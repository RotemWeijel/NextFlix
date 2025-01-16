#ifndef GET_COMMAND_H
#define GET_COMMAND_H

#include "ICommand.h"
#include <vector>
#include <iostream>
#include <algorithm>
#include "../main_body/MovieDatabase.h"
#include "../IO_handling/IOutputHandler.h"

class GetCommand : public ICommand
{
public:
    GetCommand(IOutputHandler& outputHandler);
    
    // Convert set to vector
    std::vector<int> returnArrayFromSet(const std::unordered_set<int> &set) const;

    // Phase 1 in the algorithm
    // Calculates common movies between all users and a specific user (idUser)
    void moviesInCommonForAllUsers(
        int idUser,
        MovieDatabase &database,
        std::unordered_map<int, int> &commonWithIdUser) const;

    // Phase 2 in the algorithm
    // Checks if the user has watched a particular movie
    bool hasWatchedMovie(MovieDatabase &database, int movieid, int userId) const;

    // Rates each movie, based on the users who watched it, and their similarity to the specific user
    void rateForEachMovie(
        std::unordered_map<int, int> &rateMovies, int idUser, MovieDatabase &database,
        const std::unordered_map<int, int> &commonWithIdUser,
        int idMovie) const;

    // Comparator function for sorting the movies by rate
    static bool compare(const std::pair<int, int> &a, const std::pair<int, int> &b);

    // Print recommended movies sorted by their rate
    void printRecommend(const std::unordered_map<int, int> &rateMovies) const;

    // Check validity of input arguments
    bool isValidInput(vector<int> args, MovieDatabase &database) const;
    
    std::unordered_map<int, int> declareMap(std::vector<int> some) const;

    bool contains(const std::vector<int> &vec, int value) const;

    // Algorithm implementation
    void execute(MovieDatabase &database, std::vector<std::string> args) const;

    std::string description() const override;
    virtual void printInvalid() const override;
    virtual void printIllogical() const override;
    virtual void printValid() const override;

private:
    // Store a reference to the output handler
    IOutputHandler& outputHandler_;
};

#endif // GET_COMMAND_H