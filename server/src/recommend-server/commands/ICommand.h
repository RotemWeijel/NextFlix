#ifndef I_COMMAND_H
#define I_COMMAND_H

using namespace std;
#include "../main_body/MovieDatabase.h"
#include <string>
#include <vector>

// An interface for commands that the program can execute
class ICommand {
public:
    virtual ~ICommand() = default;
    
    // An abstract function that will execute the command's operation, using the database and the elements passed in the string vector.
    virtual void execute(MovieDatabase& database, vector<string> args) const = 0;

    virtual string description() const = 0;
    virtual void printInvalid() const = 0;
    virtual void printIllogical() const = 0;
    virtual void printValid() const = 0;
};

#endif // I_COMMAND_H
