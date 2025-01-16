#include "DetachedThreadHandler.h"
#include <iostream>
#include <unistd.h>

DetachedThreadHandler::DetachedThreadHandler(MovieDatabase& database, 
                                           IDataLoader& loader, 
                                           IDataExporter& exporter)
    : database_(database)
    , loader_(loader)
    , exporter_(exporter) {}

void DetachedThreadHandler::handleConnection(int clientSocket) {
    // Thread creation and detachment
    std::thread([this, clientSocket]() {
        this->handleClient(clientSocket);
    }).detach(); // The thread runs independently of the main thread
}

void DetachedThreadHandler::handleClient(int clientSocket) {
    try {
        SocketInput sockinput(clientSocket);
        SocketOutput sockoutput(clientSocket);
        
        ClientHandler handler(
            clientSocket,
            sockinput,
            sockoutput,
            database_,
            loader_,
            exporter_
        );
        
        handler.handle();
    }
    catch (const std::exception& e) {
        std::cerr << "Error handling client: " << e.what() << std::endl;
    }
    
    close(clientSocket);
}