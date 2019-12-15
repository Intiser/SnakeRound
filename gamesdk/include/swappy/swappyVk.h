/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @defgroup swappyVk Swappy for Vulkan
 * Vulkan part of Swappy.
 * @{
 */

#pragma once

#include "swappy_common.h"

#include "jni.h"

#if (defined ANDROID) && (defined SWAPPYVK_USE_WRAPPER)
#include <vulkan_wrapper.h>
#else
#include <vulkan/vulkan.h>
#endif

#ifdef __cplusplus
extern "C" {
#endif

/**
 * @brief Determine any Vulkan device extensions that must be enabled for a new
 * VkDevice.
 *
 * Swappy-for-Vulkan (SwappyVk) benefits from certain Vulkan device extensions
 * (e.g. VK_GOOGLE_display_timing).  Before the application calls
 * vkCreateDevice, SwappyVk needs to look at the list of available extensions
 * (returned by vkEnumerateDeviceExtensionProperties) and potentially identify
 * one or more extensions that the application must add to:
 *
 * - VkDeviceCreateInfo::enabledExtensionCount
 * - VkDeviceCreateInfo::ppEnabledExtensionNames
 *
 * before the application calls vkCreateDevice.  For each VkPhysicalDevice that
 * the application will call vkCreateDevice for, the application must call this
 * function, and then must add the identified extension(s) to the list that are
 * enabled for the VkDevice.  Similar to many Vulkan functions, this function
 * can be called twice, once to identify the number of required extensions, and
 * again with application-allocated memory that the function can write into.
 *
 * @param[in]    physicalDevice          - The VkPhysicalDevice associated with the
 *                    available extensions.
 * @param[in]    availableExtensionCount - This is the returned value of
 *                    pPropertyCount from vkEnumerateDeviceExtensionProperties.
 * @param[in]    pAvailableExtensions    - This is the returned value of
 *                    pProperties from vkEnumerateDeviceExtensionProperties.
 * @param[inout] pRequiredExtensionCount - If pRequiredExtensions is nullptr, the
 *                    function sets this to the number of extensions that are
 *                    required.  If pRequiredExtensions is non-nullptr, this
 *                    is the number of required extensions that the function
 *                    should write into pRequiredExtensions.
 * @param[inout] pRequiredExtensions - If non-nullptr, this is application-allocated
 *                    memory into which the function will write the names of
 *                    required extensions.  It is a pointer to an array of
 *                    char* strings (i.e. the same as
 *                    VkDeviceCreateInfo::ppEnabledExtensionNames).
 */
void SwappyVk_determineDeviceExtensions(
    VkPhysicalDevice       physicalDevice,
    uint32_t               availableExtensionCount,
    VkExtensionProperties* pAvailableExtensions,
    uint32_t*              pRequiredExtensionCount,
    char**                 pRequiredExtensions);

/**
 * @brief Tell Swappy the queueFamilyIndex used to create a specific VkQueue
 *
 * Swappy needs to know the queueFamilyIndex used for creating a specific VkQueue
 * so it can use it when presenting.
 *
 * @param[in]  device            - The VkDevice associated with the queue
 * @param[in]  queue             - A device queue.
 * @param[in]  queueFamilyIndex  - The queue family index used to create the VkQueue.
 *
 */
void SwappyVk_setQueueFamilyIndex(
        VkDevice    device,
        VkQueue     queue,
        uint32_t    queueFamilyIndex);

// TBD: For now, SwappyVk assumes only one VkSwapchainKHR per VkDevice, and that
// applications don't re-create swapchains.  Is this long-term sufficient?

/**
 * Internal init function. Do not call directly.
 * See SwappyVk_initAndGetRefreshCycleDuration instead.
 * @private
 */
bool SwappyVk_initAndGetRefreshCycleDuration_internal(
        JNIEnv*          env,
        jobject          jactivity,
        VkPhysicalDevice physicalDevice,
        VkDevice         device,
        VkSwapchainKHR   swapchain,
        uint64_t*        pRefreshDuration);

/**
 * @brief Initialize SwappyVk for a given device and swapchain, and obtain the
 * approximate time duration between vertical-blanking periods.
 *
 * Uses JNI to query AppVsyncOffset and PresentationDeadline.
 *
 * If your application presents to more than one swapchain at a time, you must
 * call this for each swapchain before calling swappyVkSetSwapInterval() for it.
 *
 * The duration between vertical-blanking periods (an interval) is expressed as
 * the approximate number of nanoseconds between vertical-blanking periods of
 * the swapchain’s physical display.
 *
 * If the application converts this number to a fraction (e.g. 16,666,666 nsec
 * to 0.016666666) and divides one by that fraction, it will be the approximate
 * refresh rate of the display (e.g. 16,666,666 nanoseconds corresponds to a
 * 60Hz display, 11,111,111 nsec corresponds to a 90Hz display).
 *
 * @param[in]  env - JNIEnv that is assumed to be from AttachCurrentThread function
 * @param[in]  jactivity - NativeActivity object handle, used for JNI
 * @param[in]  physicalDevice   - The VkPhysicalDevice associated with the swapchain
 * @param[in]  device    - The VkDevice associated with the swapchain
 * @param[in]  swapchain - The VkSwapchainKHR the application wants Swappy to swap
 * @param[out] pRefreshDuration - The returned refresh cycle duration
 *
 * @return bool            - true if the value returned by pRefreshDuration is valid,
 *                    otherwise false if an error.
 */
static inline bool SwappyVk_initAndGetRefreshCycleDuration(
        JNIEnv*          env,
        jobject          jactivity,
        VkPhysicalDevice physicalDevice,
        VkDevice         device,
        VkSwapchainKHR   swapchain,
        uint64_t*        pRefreshDuration) {
    // This call ensures that the header and the linked library are from the same version
    // (if not, a linker error will be triggered because of an undefined symbol).
    SWAPPY_VERSION_SYMBOL();
    return SwappyVk_initAndGetRefreshCycleDuration_internal(env, jactivity, physicalDevice, device, swapchain, pRefreshDuration);
}

/**
 * @brief Tell Swappy the duration of that each presented image should be visible.
 *
 * If your application presents to more than one swapchain at a time, you must
 * call this for each swapchain before presenting to it.
 *
 * @param[in]  device    - The VkDevice associated with the swapchain
 * @param[in]  swapchain - The VkSwapchainKHR the application wants Swappy to swap
 * @param[in]  swap_ns   - The duration of that each presented image should be
 *                    visible in nanoseconds
 */
void SwappyVk_setSwapIntervalNS(
        VkDevice       device,
        VkSwapchainKHR swapchain,
        uint64_t       swap_ns);

/**
 * @brief Tell Swappy to present one or more images to corresponding swapchains.
 *
 * Swappy will call vkQueuePresentKHR for your application.  Swappy may insert a
 * struct to the pNext-chain of VkPresentInfoKHR, or it may insert other Vulkan
 * commands in order to attempt to honor the desired swap interval.
 *
 * @note If your application presents to more than one swapchain at a time, and
 * if you use a different swap interval for each swapchain, Swappy will attempt
 * to honor the swap interval for each swapchain (being more successful on
 * devices that support an underlying presentation-timing extension, such as
 * VK_GOOGLE_display_timing).
 *
 * @param[in]  queue     - The VkQueue associated with the device and swapchain
 * @param[in]  pPresentInfo - A pointer to the VkPresentInfoKHR containing the
 *                    information about what image(s) to present on which
 *                    swapchain(s).
 */
VkResult SwappyVk_queuePresent(
        VkQueue                 queue,
        const VkPresentInfoKHR* pPresentInfo);

/**
 * @brief Destroy SwappyVk instance associated to the swapchain
 *
 * This API is expected to be called before calling to vkDestroySwapchainKHR()
 * so Swappy could cleanup its internal state.
 *
 * @param[in]  device     - The VkDevice associated with SwappyVk
 * @param[in]  swapchain - The VkSwapchainKHR the application wants Swappy to swap
 */
void SwappyVk_destroySwapchain(
        VkDevice                device,
        VkSwapchainKHR          swapchain);

/**
 * @brief Enables Auto-Swap-Interval feature for all instances.
 *
 * By default this feature is enabled. Changing it is completely
 * optional for fine-tuning swappy behaviour.
 *
 * @param[in]  enabled - True means enable, false means disable
 */
void SwappyVk_setAutoSwapInterval(bool enabled);

/**
 * @brief Enables Auto-Pipeline-Mode feature for all instances.
 *
 * By default this feature is enabled. Changing it is completely
 * optional for fine-tuning swappy behaviour.
 *
 * @param[in]  enabled - True means enable, false means disable
 */
void SwappyVk_setAutoPipelineMode(bool enabled);

/**
 * @brief Sets the maximal swap duration for all instances.
 *
 * Sets the maximal duration for Auto-Swap-Interval in milliseconds.
 * If SwappyVk is operating in Auto-Swap-Interval and the frame duration is longer
 * than the provided duration, SwappyVk will not do any pacing and just submit the
 * frame as soon as possible.
 *
 * @param[in]  max_swap_ns - maximal swap duration in milliseconds.
 */
void SwappyVk_setMaxAutoSwapIntervalNS(uint64_t max_swap_ns);

/**
 * @brief The fence timeout parameter can be set for devices with faulty
 * drivers. Its default value is 50,000,000.
 */
void SwappyVk_setFenceTimeoutNS(uint64_t fence_timeout_ns);

/**
 * @brief Get the fence timeout parameter, for devices with faulty
 * drivers. Its default value is 50,000,000.
 */
uint64_t SwappyVk_getFenceTimeoutNS();

/**
 * @brief Inject callback functions to be called each frame.
 *
 * @param[in]  tracer - Collection of callback functions
 */
void SwappyVk_injectTracer(const SwappyTracer *tracer);

#ifdef __cplusplus
}  // extern "C"
#endif

/** @} */
