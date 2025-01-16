#include "GetCommand.h"
#include "../main_body/MovieDatabase.h"
#include <vector>
#include <string>
using namespace std;

GetCommand::GetCommand(IOutputHandler &outputHandler)
    : outputHandler_(outputHandler) {}

// Convert set to vector
vector<int> GetCommand::returnArrayFromSet(const unordered_set<int> &set) const
{
    vector<int> movieArray;
    for (const int &item : set)
    {
        movieArray.push_back(item);
    }
    return movieArray;
}

// Phase 1 in the algorithm
// Calculates common movies between all users and a specific user (idUser)
void GetCommand::moviesInCommonForAllUsers(
    int idUser,
    MovieDatabase &database,
    unordered_map<int, int> &commonWithIdUser) const
{
    auto moviesForUser = database.getMoviesForUser(idUser);
    for (const int &movie : moviesForUser)
    {
        auto usersForMovie = database.getUsersForMovie(movie);
        for (const int &user : usersForMovie)
        {
            if (user != idUser)
                commonWithIdUser[user] += 1;
        }
    }
}

// Phase 2 in the algorithm
// Checks if the user has watched a particular movie
bool GetCommand::hasWatchedMovie(
    MovieDatabase &database, // Changed back to use MovieDatabase
    int movieid, int userId) const
{
    const auto &moviesForUser = database.getMoviesForUser(userId);
    return moviesForUser.find(movieid) != moviesForUser.end();
}

// Rates each movie, based on the users who watched it, and their similarity to the specific user
void GetCommand::rateForEachMovie(
    unordered_map<int, int> &rateMovies, int idUser,
    MovieDatabase &database, const unordered_map<int, int> &commonWithIdUser,
    int idMovie) const
{
    const auto &usersForMovie = database.getUsersForMovie(idMovie);
    for (const int &user : usersForMovie)
    {
        const auto &moviesForUser = database.getMoviesForUser(user);
        for (const int &movie : moviesForUser)
        {
            if (!hasWatchedMovie(database, movie, idUser) && idMovie != movie)
            {
                rateMovies[movie] += commonWithIdUser.at(user);
            }
        }
    }
}

// Comparator function for sorting the movies by rate
bool GetCommand::compare(const pair<int, int> &a, const pair<int, int> &b)
{
    if (a.second != b.second)
    {
        return a.second > b.second;
    }
    return a.first < b.first;
}

// Print recommended movies sorted by their rate
void GetCommand::printRecommend(const unordered_map<int, int> &rateMovies) const
{
    vector<pair<int, int>> moviesRate(rateMovies.begin(), rateMovies.end());
    sort(moviesRate.begin(), moviesRate.end(), compare);

    std::string response = "200 Ok\n\n";

    bool hasRecommendations = false;
    int counter = 0;
    for (const auto &movie : moviesRate)
    {
        if (movie.second > 0) // if the rate of movie bigger from 0 print him
        {
            hasRecommendations = true;
            response += std::to_string(movie.first) + " ";
            if (++counter == 10)
            {
                break;
            }
        }
    }
    
    // Only add the extra newline if we actually added recommendations
    if (hasRecommendations) {
        response += "\n";
    }

    // Send everything as one message
    outputHandler_.sendOutput(response);
}

/* Helper function to check if vector contains a value */
bool GetCommand::contains(const vector<int> &vec, int value) const
{
    return find(vec.begin(), vec.end(), value) != vec.end();
}

// Check validity of input arguments
bool GetCommand::isValidInput(vector<int> args, MovieDatabase &database) const
{
    if (args.size() != 2)
    {
        printInvalid();
        return false;
    }

    // User not exist or movie not exist
    if (!database.isUserExist(args[0]) || !database.isMovieExist(args[1])) {
        printIllogical();
        return false;
    }
    
    // The user has not watched any movies
    if (database.getMoviesForUser(args[0]).empty()) {
        printIllogical();
        return false;
    }

    return true;
}

/* Declare a map from a vector, initializing all values to 0 */
unordered_map<int, int> GetCommand::declareMap(vector<int> some) const
{
    unordered_map<int, int> myMap;
    for (const int &part : some)
    {
        myMap[part] = 0;
    }
    return myMap;
}

// Algorithm implementation
void GetCommand::execute(MovieDatabase &database, vector<string> args) const
{
    // Convert string arguments to integers
    vector<int> intArgs;
    try
    {
        for (const auto &arg : args)
        {
            if (!all_of(arg.begin(), arg.end(), ::isdigit))
            {
                printInvalid();
                return;
            }
            intArgs.push_back(stoi(arg));
        }
    }
    catch (const exception &)
    {
        printInvalid();
        return;
    }

    // Lock the Database
    database.lockDatabase();
    
    if (!isValidInput(intArgs, database))
    {
        database.unlockDatabase();
        return;
    }

    int idUser = intArgs[0];
    int idMovie = intArgs[1];

    unordered_map<int, int> commonWithIdUser = declareMap(database.allUsers());

    // Filter movies that user hasn't watched
    const auto &moviesForUser = database.getMoviesForUser(idUser);
    vector<int> result;
    vector<int> movies = database.allMovies();
    copy_if(movies.begin(), movies.end(), back_inserter(result),
                 [&moviesForUser, idMovie](int value)
                 {
                     return moviesForUser.find(value) == moviesForUser.end() &&
                            value != idMovie;
                 });

    unordered_map<int, int> rateMovies = declareMap(result);

    // Phase 1
    moviesInCommonForAllUsers(idUser, database, commonWithIdUser);
    // Phase 2
    rateForEachMovie(rateMovies, idUser, database, commonWithIdUser, idMovie);

    // Unlock the Database
    database.unlockDatabase();

    // Phase 3
    printRecommend(rateMovies);

}

/* Return description of the command */
string GetCommand::description() const
{
    return "GET, arguments: [userid] [movieid]\n";
}

void GetCommand::printInvalid() const {
    outputHandler_.sendOutput("400 Bad Request\n");
}

void GetCommand::printIllogical() const {
    outputHandler_.sendOutput("404 Not Found\n");
}

void GetCommand::printValid() const {
    outputHandler_.sendOutput("200 Ok\n\n");    
}