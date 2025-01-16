#ifndef MOVIE_DATABASE_H
#define MOVIE_DATABASE_H

#include <unordered_map>
#include <unordered_set>
#include <vector>
#include <mutex>
#include "../data_handling/IDataLoader.h"
#include "../data_handling/IDataExporter.h"

// This class is responsible for managing data structures related to information about the program's users and movies.
class MovieDatabase {
public:
    MovieDatabase (IDataLoader& loader, IDataExporter& exporter);
    
    // Methods to lock and unlock the database
    void lockDatabase() const;
    void unlockDatabase() const;
    
    // Adding or deleting a movie or a user
    void addUser(int userID);
    void addMovie(int movieID);
    void addMoviesToUser(int userID, const std::vector<int>& movieIDs);
    void deleteMoviesFromUser(int userID, const std::unordered_set<int>& movieIDs);

    // Queries
    bool isUserExist(int userID);
    bool isMovieExist(int movieID);
    std::unordered_set<int> getMoviesForUser(int userID) const; // All the movies for specific user
    std::unordered_set<int> getUsersForMovie(int movieID) const; // All the users for spesific movie
    std::vector<int> allUsers() const;
    std::vector<int> allMovies() const;
    std::unordered_map<int, std::unordered_set<int>> getUserToMovies(); // The database, when the movies are the keys
    std::unordered_map<int, std::unordered_set<int>> getMovieToUsers(); // The database, when the users are the keys

private:
    std::unordered_map<int, std::unordered_set<int>> userToMovies_;
    std::unordered_map<int, std::unordered_set<int>> movieToUsers_;
    IDataLoader& loader_;
    IDataExporter& exporter_;
    mutable std::mutex mutex_;
};

#endif // MOVIE_DATABASE_H
