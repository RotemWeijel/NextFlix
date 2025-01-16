#ifndef CONSOLE_INPUT_H
#define CONSOLE_INPUT_H

#include "IInputHandler.h"
#include <string>
#include <iostream>

// Console-based input handler
class ConsoleInput : public IInputHandler {
public:
    std::string getInput() const override;
};

#endif // CONSOLE_INPUT_H
