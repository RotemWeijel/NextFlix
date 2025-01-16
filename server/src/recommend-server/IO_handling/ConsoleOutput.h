#ifndef CONSOLE_OUTPUT_H
#define CONSOLE_OUTPUT_H

#include "IOutputHandler.h"
#include <string>
#include <iostream>

// Console-based output handler
class ConsoleOutput : public IOutputHandler {
public:
    void sendOutput(const std::string& message) const override;
};

#endif // CONSOLE_OUTPUT_HANDLER_H
