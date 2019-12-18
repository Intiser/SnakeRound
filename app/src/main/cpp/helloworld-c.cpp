//
// Created by Intiser on 12/16/2019.
//
#include <jni.h>

#include "swappy/swappyGL.h"
#include "swappy/swappyGL_extra.h"
#include "Log.h"

//void SwappyGL_init(JNIEnv *env, jobject jactivity);
//void SwappyGL_destroy();
extern "C" {

void startFrameCallback(void *, int, long) {
}

void swapIntervalChangedCallback(void *) {
    uint64_t swap_ns = SwappyGL_getSwapIntervalNS();
    //ALOGI("Swappy changed swap interval to %.2fms", swap_ns / 1e6f);
}



JNIEXPORT void JNICALL
Java_project_ahsan_language_com_myapplication_ui_gameplay_GamePlayActivity_nInit(JNIEnv *env,
                                                                                 jobject activity) {
    SwappyGL_init(env, activity);

    SwappyTracer tracers;
    tracers.preWait = NULL;
    tracers.postWait = NULL;
    tracers.preSwapBuffers = NULL;
    tracers.postSwapBuffers = NULL;
    tracers.startFrame = startFrameCallback;
    //tracers.startFrame = NULL;
    tracers.userData = NULL;
    tracers.swapIntervalChanged = swapIntervalChangedCallback;
    //tracers.swapIntervalChanged = NULL;

    SwappyGL_injectTracer(&tracers);
}


JNIEXPORT void JNICALL
Java_project_ahsan_language_com_myapplication_ui_gameplay_GamePlayActivity_nDestory(JNIEnv *env,
                                                                                    jobject thiz) {

}

JNIEXPORT void JNICALL
Java_project_ahsan_language_com_myapplication_ui_gameplay_GamePlayActivity_set60FPS(JNIEnv *env,
                                                                                    jobject thiz) {
    SwappyGL_setSwapIntervalNS(SWAPPY_SWAP_60FPS);
}


};

