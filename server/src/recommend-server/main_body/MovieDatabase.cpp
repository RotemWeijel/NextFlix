#include "MovieDatabase.h"
#include "../data_handling/FileDataExporter.h"
#include "../data_handling/IDataLoader.h"
#include "../data_handling/IDataExporter.h"

MovieDatabase::MovieDatabase(IDataLoader& loader, IDataExporter& exporter)
    : loader_(loader), exporter_(exporter) {
    // Load initial data using the provided loader
    loader_.load(*this);
}

void MovieDatabase::lockDatabase() const { mutex_.lock(); }
void MovieDatabase::unlockDatabase() const { mutex_.unlock(); }

void MovieDatabase::addUser(int userID) {
    // If the user doesn't already exist, create an empty set of movies for this user
    if (userToMovies_.find(userID) == userToMovies_.end()) {
        userToMovies_[userID] = std::unordered_set<int>();
        
        // Use the exporter to add the user with an empty set of movies
        exporter_.addUser(userID, std::unordered_set<int>());
    }
}

void MovieDatabase::addMovie(int movieID) {
    // If the movie doesn't already exist, create an empty set of users for this movie
    if (movieToUsers_.find(movieID) == movieToUsers_.end()) {
        movieToUsers_[movieID] = std::unordered_set<int>();
    }
}

void MovieDatabase::addMoviesToUser(int userID, const std::vector<int>& movieIDs) {
    // Ensure the user exists
    addUser(userID);

    // Add each movie to the user's movie list and update movie-to-users mapping
    for (int movieID : movieIDs) {
        // Ensure the movie exists
        addMovie(movieID);

        // Add the movie to user's movie list
        userToMovies_[userID].insert(movieID);

        // Add the user to movie's user list
        movieToUsers_[movieID].insert(userID);
    }

    // Use the exporter to update movies for this user
    exporter_.addMoviesToUser(userID, userToMovies_[userID]);
}

bool MovieDatabase::isUserExist(int userID) {
    return userToMovies_.find(userID) != userToMovies_.end();
}

bool MovieDatabase::isMovieExist(int movieID) {
    return movieToUsers_.find(movieID) != movieToUsers_.end();
}

std::unordered_set<int> MovieDatabase::getMoviesForUser(int userID) const {
    // Find the user's movies, return empty set if user not found
    auto it = userToMovies_.find(userID);
    return (it != userToMovies_.end()) ? it->second : std::unordered_set<int>();
}

std::unordered_set<int> MovieDatabase::getUsersForMovie(int movieID) const {
    // Find the movie's users, return empty set if movie not found
    auto it = movieToUsers_.find(movieID);
    return (it != movieToUsers_.end()) ? it->second : std::unordered_set<int>();
}

std::vector<int> MovieDatabase::allUsers() const {
    std::vector<int> users;
    users.reserve(userToMovies_.size());
    
    // Collect all user IDs
    for (const auto& pair : userToMovies_) {
        users.push_back(pair.first);
    }
    
    return users;
}

std::vector<int> MovieDatabase::allMovies() const {
    std::vector<int> movies;
    movies.reserve(movieToUsers_.size());
    
    // Collect all movie IDs
    for (const auto& pair : movieToUsers_) {
        movies.push_back(pair.first);
    }
    
    return movies;
}

std::unordered_map<int, std::unordered_set<int>> MovieDatabase::getUserToMovies() {
    return userToMovies_;
}

std::unordered_map<int, std::unordered_set<int>> MovieDatabase::getMovieToUsers() {
    return movieToUsers_;
}

void MovieDatabase::deleteMoviesFromUser(int userID, const std::unordered_set<int>& movieIDs) {
    // Check if the user exists
    auto userIt = userToMovies_.find(userID);
    if (userIt == userToMovies_.end()) {
        return;  // User doesn't exist, nothing to do
    }

    // Iterate through the movies to delete
    for (int movieID : movieIDs) {
        // Check if the user has this movie
        auto& userMovies = userIt->second;
        if (userMovies.find(movieID) != userMovies.end()) {
            // Remove the movie from user's movie list
            userMovies.erase(movieID);

            // Remove the user from the movie's user list
            auto movieIt = movieToUsers_.find(movieID);
            if (movieIt != movieToUsers_.end()) {
                movieIt->second.erase(userID);
            }
        }
    }

    // Update the file using the exporter
    exporter_.deleteMoviesFromUser(userID, movieIDs);
}