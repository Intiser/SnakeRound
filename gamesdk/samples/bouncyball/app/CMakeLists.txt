cmake_minimum_required(VERSION 3.4.1)

set( CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14 -Werror -Wthread-safety -D _LIBCPP_ENABLE_THREAD_SAFETY_ANNOTATIONS" )

# ============== Games SDK

# This uses Swappy as a static library from the Game SDK package. Make sure the Android
# SDK and NDK versions that you are using are supported by the Game SDK.
include("../../gamesdk.cmake")
add_gamesdk_target(PACKAGE_DIR "../../../" BUILD_TYPE "Release")

# ============== Bouncy Ball

include_directories( ../../common/include )
include_directories( src/main/cpp )

add_library( native-lib

             SHARED

             src/main/cpp/Circle.cpp
             src/main/cpp/Orbit.cpp
             src/main/cpp/Renderer.cpp
             src/main/cpp/Settings.cpp
             ../../common/src/Thread.cpp
        )

target_link_libraries( native-lib

                       android
                       EGL
                       GLESv2
                       log
                       gamesdk
                       )
