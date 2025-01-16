#include "PostCommand.h"
#include "../main_body/MovieDatabase.h"
#include <iostream>

PostCommand::PostCommand(IOutputHandler &outputHandler)
    : outputHandler_(outputHandler) {}

void PostCommand::execute(MovieDatabase& database, std::vector<std::string> args) const {
    std::vector<long int> newInput = convertInputToInt(args);


    // Check input validity
    if (!isValidInput(newInput)){
        return;
    }

    // Lock the Database
    database.lockDatabase();

    // POST adds a user only if it does not exist
    if (database.isUserExist(newInput[0])){
        printIllogical();
        database.unlockDatabase();
        return;
    }

    // Add the new user to the database
    database.addUser(newInput[0]);
    
    // Create a vector of movie IDs (excluding the first element which is the user ID)
    std::vector<int> movieIDs(newInput.begin() + 1, newInput.end());
    
    // Call addMoviesToUser with the user ID and vector of movie IDs
    database.addMoviesToUser(newInput[0], movieIDs);

    // Unlock the Database
    database.unlockDatabase();

    printValid();
}

std::vector<long int> PostCommand::convertInputToInt(const std::vector<std::string>& args) const {
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

bool PostCommand::isValidInput(std::vector<long int> args) const {
    // Check if there are at least two arguments
    if (args.size() < 2) {
        printInvalid();
        return false; 
    }
    //Check if all arguments are positive
    for (int arg: args)
    {
        if (arg < 0) {
            printInvalid();
            return false;
        }
    }
    
    return true;
}

std::string PostCommand::description() const {
    return "POST, arguments: [userid] [movieid1] [movieid2] ...\n";
}

void PostCommand::printInvalid() const {
    outputHandler_.sendOutput("400 Bad Request\n");
}

void PostCommand::printIllogical() const {
    outputHandler_.sendOutput("404 Not Found\n");
}

void PostCommand::printValid() const {
    outputHandler_.sendOutput("201 Created\n");    
}