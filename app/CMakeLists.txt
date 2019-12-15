cmake_minimum_required(VERSION 3.6.0)


include("../gamesdk/samples/gamesdk.cmake")
add_gamesdk_target(PACKAGE_DIR "../gamesdk" BUILD_TYPE "Release")

include_directories( ../../common/include )
include_directories( src/main/cpp )

add_library(native_lib
    SHARED

    src/main/cpp/helloworld-c.cpp)


target_link_libraries(native_lib
        gamesdk)