cmake_minimum_required(VERSION 3.4.1)
project(FoodScanner)
add_library(
        api-keys
        SHARED
        src/main/cpp/api-key.cpp )
find_library(
        log-lib
        log )
target_link_libraries(
        api-keys
        ${log-lib} )
