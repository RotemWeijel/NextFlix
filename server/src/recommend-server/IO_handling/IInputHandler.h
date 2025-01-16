#ifndef I_INPUT_HANDLER_H
#define I_INPUT_HANDLER_H

#include <string>

// Interface for input handling
class IInputHandler {
public:
    virtual ~IInputHandler() = default;
    virtual std::string getInput() const = 0; // Get input from the source
};

#endif // I_INPUT_HANDLER_H
