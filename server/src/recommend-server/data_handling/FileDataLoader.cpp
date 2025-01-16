#include "FileDataLoader.h"
#include "../main_body/MovieDatabase.h"
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <filesystem>
#include <vector>

FileDataLoader::FileDataLoader(const std::string& directoryPath) : directoryPath_(directoryPath) {}

void FileDataLoader::load(MovieDatabase& database) const {
    // Iterate through all files in the directory
    for (const auto& entry : std::filesystem::directory_iterator(directoryPath_)) {
        std::string filename = entry.path().filename().string();
        
        // Check if it's a user or movie file
        if (filename.substr(0, 5) == "user_" && filename.substr(filename.length() - 4) == ".txt") {
            loadUserFile(entry.path().string(), database);
        }
        else if (filename.substr(0, 6) == "movie_" && filename.substr(filename.length() - 4) == ".txt") {
            loadMovieFile(entry.path().string(), database);
        }
    }
}

void FileDataLoader::loadUserFile(const std::string& filepath, MovieDatabase& database) const {
    std::ifstream file(filepath);
    if (!file.is_open()) {
        throw std::runtime_error("Unable to open user file: " + filepath);
    }

    int userID;
    file >> userID;  // First line is the user ID

    // Add the user to the database
    database.addUser(userID);

    // Read movie IDs for this user
    std::vector<int> movieIDs;
    int movieID;
    while (file >> movieID) {
        movieIDs.push_back(movieID);
    }

    // Add all movies to the user at once
    database.addMoviesToUser(userID, movieIDs);

    file.close();
}

void FileDataLoader::loadMovieFile(const std::string& filepath, MovieDatabase& database) const {
    std::ifstream file(filepath);
    if (!file.is_open()) {
        throw std::runtime_error("Unable to open movie file: " + filepath);
    }

    int movieID;
    file >> movieID;  // First line is the movie ID

    // Add the movie to the database
    database.addMovie(movieID);

    // Read user IDs for this movie
    std::vector<int> userIDs;
    int userID;
    while (file >> userID) {
        userIDs.push_back(userID);
    }

    // Add all users to the movie at once
    for (int userId : userIDs) {
        database.addMoviesToUser(userId, {movieID});
    }

    file.close();
}