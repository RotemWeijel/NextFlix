#include "ConsoleOutput.h"

void ConsoleOutput::sendOutput(const std::string& message) const {
    std::cout << message;
}