#include "FileDataExporter.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <filesystem>

namespace fs = std::filesystem;

FileDataExporter::FileDataExporter(const std::string& filePath) 
    : filePath_(filePath) 
{
    // Ensure the data directory exists
    if (!fs::exists(filePath_)) {
        if (!fs::create_directory(filePath_)) {
            return;
        }
    }
}

void FileDataExporter::addUser(int userId, std::unordered_set<int> movies) {
    // Construct the file path for the user 
    std::string userFilePath = filePath_ + "/user_" + std::to_string(userId) + ".txt";
    
    // Open the file in write mode, this will overwrite any existing file
    std::ofstream outFile(userFilePath);
    if (outFile.is_open()) {
        // Write user ID on the first line
        outFile << userId << std::endl;

        // Write movie IDs on the second line, separated by spaces
        for (auto it = movies.begin(); it != movies.end(); ++it) {
            if (it != movies.begin()) {
                outFile << " ";  // Add a space between movie IDs
            }
            outFile << *it;
        }
        outFile << std::endl; // End the line after all movie IDs

        outFile.close();
    }
}

void FileDataExporter::addMoviesToUser(int userId, std::unordered_set<int> movies) {
    // Construct the file path for the user
    std::string userFilePath = filePath_ + "/user_" + std::to_string(userId) + ".txt";
    
    // Open the file in read mode first to read existing data
    std::ifstream inFile(userFilePath);
    std::unordered_set<int> existingMovies;

    if (inFile.is_open()) {
        std::string userIdLine, moviesLine;
        std::getline(inFile, userIdLine);  // Read the user ID line
        
        // Read the existing movie IDs (if any)
        std::getline(inFile, moviesLine);
        std::istringstream iss(moviesLine);
        int movieId;
        while (iss >> movieId) {
            existingMovies.insert(movieId);  // Store existing movie IDs in a set
        }
        inFile.close();  // Close the input file
    }

    // Merge the new movies into the existing set
    existingMovies.insert(movies.begin(), movies.end());

    // Open the file in write mode to overwrite with updated data
    std::ofstream outFile(userFilePath);
    if (outFile.is_open()) {
        outFile << userId << std::endl;  // Write user ID on the first line

        // Write the updated movie IDs on the second line
        for (auto it = existingMovies.begin(); it != existingMovies.end(); ++it) {
            if (it != existingMovies.begin()) {
                outFile << " ";  // Add a space between movie IDs
            }
            outFile << *it;
        }
        outFile << std::endl;  // End the line after writing all movie IDs

        outFile.close();
    } else {
        std::cerr << "Failed to open file: " << userFilePath << std::endl;
    }
}


void FileDataExporter::deleteMoviesFromUser(int userId, std::unordered_set<int> movieIds) {
    // Construct the file path for the user
    std::string userFilePath = filePath_ + "/user_" + std::to_string(userId) + ".txt";
    
    // Open the file in read mode first to read existing data
    std::ifstream inFile(userFilePath);
    std::unordered_set<int> existingMovies;

    if (inFile.is_open()) {
        std::string userIdLine, moviesLine;
        std::getline(inFile, userIdLine);  // Read the user ID line
        
        // Read the existing movie IDs
        std::getline(inFile, moviesLine);
        std::istringstream iss(moviesLine);
        int movieId;
        while (iss >> movieId) {
            existingMovies.insert(movieId);  // Store existing movie IDs in a set
        }
        inFile.close();  // Close the input file
    }

    // Remove the specified movies
    for (int movieId : movieIds) {
        existingMovies.erase(movieId);
    }

    // Open the file in write mode to overwrite with updated data
    std::ofstream outFile(userFilePath);
    if (outFile.is_open()) {
        outFile << userId << std::endl;  // Write user ID on the first line

        // Write the updated movie IDs on the second line
        for (auto it = existingMovies.begin(); it != existingMovies.end(); ++it) {
            if (it != existingMovies.begin()) {
                outFile << " ";  // Add a space between movie IDs
            }
            outFile << *it;
        }
        outFile << std::endl;  // End the line after writing all movie IDs

        outFile.close();
    } else {
        std::cerr << "Failed to open file: " << userFilePath << std::endl;
    }
}
