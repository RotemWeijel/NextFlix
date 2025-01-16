#ifndef I_OUTPUT_HANDLER_H
#define I_OUTPUT_HANDLER_H

#include <string>

// Interface for output handling
class IOutputHandler {
public:
    virtual ~IOutputHandler() = default;
    virtual void sendOutput(const std::string& message) const = 0; // Send output to the destination
};

#endif // I_OUTPUT_HANDLER_H
