#ifndef DETACHED_THREAD_HANDLER_H
#define DETACHED_THREAD_HANDLER_H

#include "IThreadHandler.h"
#include "../main_body/ClientHandler.h"
#include "../IO_handling/SocketInput.h"
#include "../IO_handling/SocketOutput.h"
#include "../main_body/MovieDatabase.h"
#include <thread>
#include <memory>

class DetachedThreadHandler : public IThreadHandler {
private:
    MovieDatabase& database_;
    IDataLoader& loader_;
    IDataExporter& exporter_;

    void handleClient(int clientSocket);

public:
    DetachedThreadHandler(MovieDatabase& database, 
                         IDataLoader& loader, 
                         IDataExporter& exporter);
    
    void handleConnection(int clientSocket) override;
};

#endif // DETACHED_THREAD_HANDLER_H