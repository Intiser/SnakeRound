cmake_minimum_required(VERSION 3.6.0)

set( CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14 -Werror -Wthread-safety -D _LIBCPP_ENABLE_THREAD_SAFETY_ANNOTATIONS" )
include("../gamesdk/samples/gamesdk.cmake")
add_gamesdk_target(PACKAGE_DIR "../gamesdk" BUILD_TYPE "Release")

include_directories( ../../common/include )
include_directories( src/main/cpp )
include_directories( J:/android-games-sdk/gamesdk/samples/common/include )

add_library(native-lib
    SHARED

    src/main/cpp/helloworld-c.cpp
        src/main/jni/native-lib.cpp
        )


target_link_libraries(native-lib
        android
        EGL
        GLESv2
        log
        gamesdk)