#ifndef HELP_COMMAND_H
#define HELP_COMMAND_H

#include "ICommand.h"
#include "../IO_handling/IOutputHandler.h"
#include <iostream>
#include <unordered_map>
#include <algorithm>

// Command for displaying available commands
class HelpCommand : public ICommand {
public:
    // Constructor that takes references to commands map and output handler
    HelpCommand(const std::unordered_map<std::string, ICommand*>& commands,
                IOutputHandler& outputHandler, std::string helpCommandName);
    
    // Execute the help command
    void execute(MovieDatabase& database, std::vector<std::string> args) const override;
    
    // Check if input arguments are valid (should be empty)
    bool isValidInput(const std::vector<std::string>& args) const;

    std::string description() const override;
    virtual void printInvalid() const override;
    virtual void printIllogical() const override;
    virtual void printValid() const override;

private:
    // Store a reference to the commands map
    const std::unordered_map<std::string, ICommand*>& commands_;
    
    // Store a reference to the output handler
    IOutputHandler& outputHandler_;

    // Store a reference to help command name
    std::string helpCommandName_;
};

#endif // HELP_COMMAND_H
