#include "Application.h"
#include "MovieDatabase.h"
#include <sstream>
#include <vector>
#include <algorithm>
#include <iterator>
#include <iostream>

// Constructor
Application::Application(
    IInputHandler& inputHandler, 
    IOutputHandler& outputHandler, 
    std::unordered_map<std::string, ICommand*>& commands,
    IDataLoader& loader,
    IDataExporter& exporter,
    MovieDatabase& database)
    : inputHandler_(inputHandler), 
      outputHandler_(outputHandler), 
      commands_(commands),
      loader_(loader),
      exporter_(exporter),
      database_(database)
{
    
}

// Helper function to split a line into words, filtering spaces
std::vector<std::string> splitInput(const std::string& line) {
    std::istringstream stream(line);
    std::vector<std::string> tokens;
    std::copy(std::istream_iterator<std::string>(stream),
              std::istream_iterator<std::string>(),
              std::back_inserter(tokens));
    return tokens;
}

// Main loop
void Application::run() {
    while (true) {
        try {
            std::string line = inputHandler_.getInput();
            std::vector<std::string> tokens = splitInput(line);
            if (tokens.empty()) {
                continue;
            }
            std::string commandName = tokens[0];
            tokens.erase(tokens.begin());
            
            auto it = commands_.find(commandName);
            if (it != commands_.end()) {
                try {
                    it->second->execute(database_, tokens);
                } catch (const std::exception& e) {
                    outputHandler_.sendOutput("400 Bad Request\n");
                }
            } else {
                outputHandler_.sendOutput("400 Bad Request\n");
            }
        } catch (const std::runtime_error& e) {
            if (std::string(e.what()) == "Connection closed by client") {
                return;
            }
        }
    }
}
