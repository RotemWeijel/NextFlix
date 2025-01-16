#include "ConsoleInput.h"
#include <iostream>
#include <limits>

std::string ConsoleInput::getInput() const {
    std::string input;
    
    // Wait for non-empty input
    while (true) {
        // Clear any previous error states
        std::cin.clear();
        
        // Read a full line of input
        if (std::getline(std::cin, input)) {
            // Remove leading and trailing whitespace
            input.erase(0, input.find_first_not_of(" \t"));
            input.erase(input.find_last_not_of(" \t") + 1);
            
            // If input is not empty, return it
            if (!input.empty()) {
                return input;
            }
        }
        
        // If input failed or was empty, clear input and try again
        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    }
}