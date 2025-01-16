#include "DeleteCommand.h"
#include "../main_body/MovieDatabase.h"
#include <iostream>

DeleteCommand::DeleteCommand(IOutputHandler &outputHandler)
    : outputHandler_(outputHandler) {}

void DeleteCommand::execute(MovieDatabase& database, std::vector<std::string> args) const {
    std::vector<long int> newInput = convertInputToInt(args);

    // Check input validity
    if (!isValidInput(newInput)) {
        return;
    }

    // Lock the Database
    database.lockDatabase();
    
    // Check if user exists
    if (!database.isUserExist(newInput[0])) {
        printIllogical();
        database.unlockDatabase();
        return;
    }

    // Check if all movies exist
    for (size_t i = 1; i < newInput.size(); ++i) {
        if (!database.isMovieExist(newInput[i])) {
            printIllogical();
            database.unlockDatabase();
            return;
        }
    }

    // Check if all specified movies exist for the user
    std::unordered_set<int> userMovies = database.getMoviesForUser(newInput[0]);
    for (size_t i = 1; i < newInput.size(); ++i) {
        if (userMovies.find(newInput[i]) == userMovies.end()) {
            printIllogical();
            database.unlockDatabase();
            return;
        }
    }

    // Create a set of movie IDs to delete (excluding the first element which is the user ID)
    std::unordered_set<int> movieIDsToDelete(newInput.begin() + 1, newInput.end());
    
    // Call deleteMoviesFromUser with the user ID and set of movie IDs
    database.deleteMoviesFromUser(newInput[0], movieIDsToDelete);

    // Unlock the Database
    database.unlockDatabase();

    printValid();
}

std::vector<long int> DeleteCommand::convertInputToInt(const std::vector<std::string>& args) const {
    std::vector<long int> result;

    for (const auto& str : args) {
        try {
            long int number = std::stol(str);  // Convert string to int
            result.push_back(number);
        } catch (const std::invalid_argument& e) {
            return {};
        } catch (const std::out_of_range& e) {
            return {};
        }
    }
    
    return result;
}

bool DeleteCommand::isValidInput(std::vector<long int> args) const {
    // Check if there are at least two arguments
    if (args.size() < 2) {
        printInvalid();
        return false; 
    }

    // Check if all arguments are positive
    for (int arg: args) {
        if (arg < 0) {
            printInvalid();
            return false;
        }
    }
    
    return true;
}

std::string DeleteCommand::description() const {
    return "DELETE, arguments: [userid] [movieid1] [movieid2] ...\n";
}

void DeleteCommand::printInvalid() const {
    outputHandler_.sendOutput("400 Bad Request\n");
}

void DeleteCommand::printIllogical() const {
    outputHandler_.sendOutput("404 Not Found\n");
}

void DeleteCommand::printValid() const {
    outputHandler_.sendOutput("204 No Content\n");    
}