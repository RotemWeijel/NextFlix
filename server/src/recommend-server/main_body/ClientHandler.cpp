#include "ClientHandler.h"

ClientHandler::ClientHandler(int clientSocket,
                             SocketInput &sockinput,
                             SocketOutput &sockoutput,
                             MovieDatabase &database,
                             IDataLoader &loader,
                             IDataExporter &exporter)
    : clientSocket(clientSocket), sockinput(sockinput), database(database),
      sockoutput(sockoutput), fileLoader(loader), fileExporter(exporter)
{
    initializeCommands();
    app = new Application(sockinput,
                          sockoutput,
                          commands,
                          fileLoader,
                          fileExporter,
                          database);
}

ClientHandler::~ClientHandler()
{
    for (auto &pair : commands)
    {
        delete pair.second;
    }
    delete app; // Socket is closed in handle()
}

    void ClientHandler::initializeCommands()
    {
        commands["POST"] = new PostCommand(sockoutput);
        commands["PATCH"] = new PatchCommand(sockoutput);
        commands["DELETE"] = new DeleteCommand(sockoutput);
        commands["GET"] = new GetCommand(sockoutput);
        commands["help"] = new HelpCommand(commands, sockoutput, "help");
    }

    void ClientHandler::handle()
    {
        try
        {
            app->run();
        }
        catch (const std::exception &e)
        {
            std::cerr << "ClientHandler: Exception caught: " << e.what() << std::endl;
        }
        close(clientSocket);
    }
