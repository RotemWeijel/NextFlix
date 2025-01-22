cmake_minimum_required(VERSION 3.14)
project(netflix)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Include directories
include_directories(
    ${PROJECT_SOURCE_DIR}/src/recommend-server
    ${PROJECT_SOURCE_DIR}/src/recommend-server/main_body
    ${PROJECT_SOURCE_DIR}/src/recommend-server/commands
    ${PROJECT_SOURCE_DIR}/src/recommend-server/IO_handling
    ${PROJECT_SOURCE_DIR}/src/recommend-server/data_handling
    ${PROJECT_SOURCE_DIR}/src/recommend-server/thread_handling
)

# Common source files
set(COMMON_SOURCES
    src/recommend-server/main_body/Application.cpp
    src/recommend-server/main_body/MovieDatabase.cpp
    src/recommend-server/main_body/ClientHandler.cpp
    src/recommend-server/commands/DeleteCommand.cpp
    src/recommend-server/commands/GetCommand.cpp
    src/recommend-server/commands/HelpCommand.cpp
    src/recommend-server/commands/PatchCommand.cpp
    src/recommend-server/commands/PostCommand.cpp
    src/recommend-server/data_handling/FileDataLoader.cpp
    src/recommend-server/data_handling/FileDataExporter.cpp
    src/recommend-server/IO_handling/ConsoleInput.cpp
    src/recommend-server/IO_handling/ConsoleOutput.cpp
    src/recommend-server/IO_handling/SocketInput.cpp
    src/recommend-server/IO_handling/SocketOutput.cpp
    src/recommend-server/thread_handling/DetachedThreadHandler.cpp
    src/recommend-server/thread_handling/ThreadPool.cpp
)

# Server executable
add_executable(NetflixServer 
    src/recommend-server/main_body/Server.cpp
    ${COMMON_SOURCES}
)
target_link_libraries(NetflixServer pthread)