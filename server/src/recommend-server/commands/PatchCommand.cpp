#include "PatchCommand.h"
#include "../main_body/MovieDatabase.h"
#include <iostream>

PatchCommand::PatchCommand(IOutputHandler &outputHandler)
    : outputHandler_(outputHandler) {}

void PatchCommand::execute(MovieDatabase& database, std::vector<std::string> args) const {
    std::vector<long int> newInput = convertInputToInt(args);


    // Check input validity
    if (!isValidInput(newInput)){
        return;
    }

    // Lock the Database
    database.lockDatabase();

    // PATCH only updates a user if it already exist
    if (!database.isUserExist(newInput[0])){
        printIllogical();
        database.unlockDatabase();
        return;
    }
    
    // Create a vector of movie IDs (excluding the first element which is the user ID)
    std::vector<int> movieIDs(newInput.begin() + 1, newInput.end());
    
    // Call addMoviesToUser with the user ID and vector of movie IDs
    database.addMoviesToUser(newInput[0], movieIDs);

    // Unlock the Database
    database.unlockDatabase();

    printValid();
}

std::vector<long int> PatchCommand::convertInputToInt(const std::vector<std::string>& args) const {
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

bool PatchCommand::isValidInput(std::vector<long int> args) const {
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

std::string PatchCommand::description() const {
    return "PATCH, arguments: [userid] [movieid1] [movieid2] ...\n";
}

void PatchCommand::printInvalid() const {
    outputHandler_.sendOutput("400 Bad Request\n");
}

void PatchCommand::printIllogical() const {
    outputHandler_.sendOutput("404 Not Found\n");
}

void PatchCommand::printValid() const {
    outputHandler_.sendOutput("204 No Content\n");    
}