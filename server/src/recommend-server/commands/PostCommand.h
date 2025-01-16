#ifndef POST_COMMAND_H
#define POST_COMMAND_H

#include "ICommand.h"
#include "../IO_handling/IOutputHandler.h"
#include <string>
#include <fstream>
#include <vector>
#include <sstream>

class PostCommand : public ICommand {
public:
    PostCommand(IOutputHandler& outputHandler);
    void execute(MovieDatabase& database, std::vector<std::string> args) const override;
    std::string description() const override;
    virtual void printInvalid() const override;
    virtual void printIllogical() const override;
    virtual void printValid() const override;
    std::vector<long int> convertInputToInt(const std::vector<std::string>& args) const;

private:
    // Store a reference to the output handler
    IOutputHandler& outputHandler_;
    bool isValidInput(std::vector<long int> args) const;
};

#endif // POST_COMMAND_H