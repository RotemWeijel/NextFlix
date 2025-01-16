#ifndef CLIENT_HANDLER_H
#define CLIENT_HANDLER_H

#include "../main_body/Application.h"
#include "../main_body/MovieDatabase.h"
#include "../data_handling/FileDataLoader.h"
#include "../data_handling/FileDataExporter.h"
#include "../IO_handling/SocketInput.h"
#include "../IO_handling/SocketOutput.h"
#include "../commands/PostCommand.h"
#include "../commands/PatchCommand.h"
#include "../commands/DeleteCommand.h"
#include "../commands/GetCommand.h"
#include "../commands/HelpCommand.h"
#include <unistd.h>

class ClientHandler
{
public:
    ClientHandler(int clientSocket,
                  SocketInput &sockinput,
                  SocketOutput &sockoutput,
                  MovieDatabase &database,
                  IDataLoader &loader,
                  IDataExporter &exporter);

    ~ClientHandler();
    void initializeCommands();
    void handle();

private:
    int clientSocket;
    MovieDatabase &database;
    IDataLoader &fileLoader;
    IDataExporter &fileExporter;
    SocketInput &sockinput;
    SocketOutput &sockoutput;
    std::unordered_map<std::string, ICommand *> commands;
    Application *app;
};

#endif // CLIENT_HANDLER_H