#include "HelpCommand.h"

// Constructor
HelpCommand::HelpCommand(
    const std::unordered_map<std::string, ICommand*>& commands,
    IOutputHandler& outputHandler, std::string helpCommandName)
    : commands_(commands), outputHandler_(outputHandler),
    helpCommandName_(helpCommandName) {}

// Execute method
void HelpCommand::execute(MovieDatabase& database, std::vector<std::string> args) const {
   
   // Validate the input
   if (!isValidInput(args)) {
       printInvalid();
       return;
   }

   std::string response = "200 Ok\n\n";  // Start with status code

   // Create a vector of command names for sorting
   std::vector<std::string> sortedCommandNames;
   for (const auto& pair : commands_) {
       if (pair.first != helpCommandName_) {
           sortedCommandNames.push_back(pair.first);
       }
   }

   // Sort command names
   std::sort(sortedCommandNames.begin(), sortedCommandNames.end());

   // Add sorted commands to response
   for (const auto& cmdName : sortedCommandNames) {
       response += commands_.at(cmdName)->description();
   }

   // Add help command last
   response += commands_.at(helpCommandName_)->description();

   // Send complete response
   outputHandler_.sendOutput(response);
}

// Validate input method
bool HelpCommand::isValidInput(const std::vector<std::string>& args) const {
    // Help command should have no arguments
    return args.empty();
}

std::string HelpCommand::description() const {
    return "help\n";
}

void HelpCommand::printInvalid() const {
    outputHandler_.sendOutput("400 Bad Request\n");
}

void HelpCommand::printIllogical() const {
    outputHandler_.sendOutput("404 Not Found\n");
}

void HelpCommand::printValid() const {
    outputHandler_.sendOutput("200 Ok\n\n");    
}