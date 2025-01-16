#ifndef APPLICATION_H
#define APPLICATION_H

using namespace std;
#include <unordered_map>
#include <string>
#include "MovieDatabase.h"
#include "../commands/ICommand.h"
#include "../IO_handling/IInputHandler.h"
#include "../IO_handling/IOutputHandler.h"
#include "../data_handling/IDataLoader.h"
#include "../data_handling/IDataExporter.h"

// This class is responsible for initializing the program, loading the information and running the main loop.
class Application {
public:
    Application(
                IInputHandler& inputHandler, 
                IOutputHandler& outputHandler, 
                unordered_map<string, ICommand*>& commands,
                IDataLoader& loader,
                IDataExporter& exporter,
                MovieDatabase& database);

    void run();

private:
    IInputHandler& inputHandler_;
    IOutputHandler& outputHandler_;
    unordered_map<string, ICommand*>& commands_;
    IDataLoader& loader_;
    IDataExporter& exporter_;
    MovieDatabase& database_;
};

#endif // APPLICATION_H
