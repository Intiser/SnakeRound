/*
 * Copyright 2019 The Android Open Source Project
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
 * @defgroup swappy_common Swappy common tools
 * Tools to be used with Swappy for OpenGL or Swappy for Vulkan.
 * @{
 */

#pragma once

#include <stdint.h>

/** @brief Swap interval for 60fps, in nanoseconds. */
#define SWAPPY_SWAP_60FPS (16666667L)

/** @brief Swap interval for 30fps, in nanoseconds. */
#define SWAPPY_SWAP_30FPS (33333333L)

/** @brief Swap interval for 20fps, in nanoseconds. */
#define SWAPPY_SWAP_20FPS (50000000L)

/** @cond INTERNAL */

#define SWAPPY_SYSTEM_PROP_KEY_DISABLE "swappy.disable"

// Internal macros to track Swappy version, do not use directly.
#define SWAPPY_MAJOR_VERSION 1
#define SWAPPY_MINOR_VERSION 0
#define SWAPPY_PACKED_VERSION ((SWAPPY_MAJOR_VERSION<<16)|(SWAPPY_MINOR_VERSION))

// Internal macros to generate a symbol to track Swappy version, do not use directly.
#define SWAPPY_VERSION_CONCAT_NX(PREFIX, MAJOR, MINOR) PREFIX ## _ ## MAJOR ## _ ## MINOR
#define SWAPPY_VERSION_CONCAT(PREFIX, MAJOR, MINOR) SWAPPY_VERSION_CONCAT_NX(PREFIX, MAJOR, MINOR)
#define SWAPPY_VERSION_SYMBOL SWAPPY_VERSION_CONCAT(Swappy_version, SWAPPY_MAJOR_VERSION, SWAPPY_MINOR_VERSION)

#ifdef __cplusplus
extern "C" {
#endif

// Internal function to track Swappy version bundled in a binary. Do not call directly.
// If you are getting linker errors related to Swappy_version_x_y, you probably have a
// mismatch between the header used at compilation and the actually library used by the linker.
void SWAPPY_VERSION_SYMBOL();

/** @endcond */

/**
 * @brief Return the version of the Swappy library at runtime.
 */
uint32_t Swappy_version();

#ifdef __cplusplus
}  // extern "C"
#endif

/**
 * Pointer to a function that can be attached to SwappyTracer::preWait or SwappyTracer::postWait.
 * @param userData Pointer to arbitrary data, see SwappyTracer::userData.
 */
typedef void (*SwappyWaitCallback)(void*);

/**
 * Pointer to a function that can be attached to SwappyTracer::preSwapBuffers.
 * @param userData Pointer to arbitrary data, see SwappyTracer::userData.
 */
typedef void (*SwappyPreSwapBuffersCallback)(void*);

/**
 * Pointer to a function that can be attached to SwappyTracer::postSwapBuffers.
 * @param userData Pointer to arbitrary data, see SwappyTracer::userData.
 * @param desiredPresentationTimeMillis The target time, in milliseconds, at which the frame
 * would be presented on screen.
 */
typedef void (*SwappyPostSwapBuffersCallback)(void*, long desiredPresentationTimeMillis);

/**
 * Pointer to a function that can be attached to SwappyTracer::startFrame.
 * @param userData Pointer to arbitrary data, see SwappyTracer::userData.
 * @param desiredPresentationTimeMillis The time, in milliseconds, at which the frame is scheduled to be presented.
 */
typedef void (*SwappyStartFrameCallback)(void*, int currentFrame, long desiredPresentationTimeMillis);

/**
 * Pointer to a function that can be attached to SwappyTracer::swapIntervalChanged.
 * Call ::SwappyGL_getSwapIntervalNS or ::SwappyVk_getSwapIntervalNS to get the latest swapInterval.
 * @param userData Pointer to arbitrary data, see SwappyTracer::userData.
 */
typedef void (*SwappySwapIntervalChangedCallback)(void*);

/**
 * @brief Collection of callbacks to be called each frame to trace execution.
 *
 * Injection of these is optional.
 */
typedef struct SwappyTracer {
    /**
     * Callback called before waiting to queue the frame to the composer.
     */
    SwappyWaitCallback preWait;

    /**
     * Callback called after wait to queue the frame to the composer is done.
     */
    SwappyWaitCallback postWait;

    /**
     * Callback called before calling the function to queue the frame to the composer.
     */
    SwappyPreSwapBuffersCallback preSwapBuffers;

    /**
     * Callback called after calling the function to queue the frame to the composer.
     */
    SwappyPostSwapBuffersCallback postSwapBuffers;

    /**
     * Callback called at the start of a frame.
     */
    SwappyStartFrameCallback startFrame;

    /**
     * Pointer to some arbitrary data that will be passed as the first argument
     * of callbacks.
     */
    void* userData;

    /**
     * Callback called when the swap interval was changed.
     */
    SwappySwapIntervalChangedCallback swapIntervalChanged;
} SwappyTracer;

/** @} */
