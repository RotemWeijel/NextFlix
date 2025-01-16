#ifndef THREAD_POOL_H
#define THREAD_POOL_H

#include "IThreadHandler.h"
#include "../main_body/ClientHandler.h"
#include "../IO_handling/SocketInput.h"
#include "../IO_handling/SocketOutput.h"
#include "../main_body/MovieDatabase.h"
#include <vector>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <functional>

class ThreadPool : public IThreadHandler {
public:
    ThreadPool(size_t numThreads, MovieDatabase& database, 
               IDataLoader& loader, IDataExporter& exporter);
    ~ThreadPool();
    
    void handleConnection(int clientSocket) override;
    void stop();

private:
    void workerFunction();
    void handleClient(int clientSocket);

    std::vector<std::thread> workers;
    std::queue<int> tasks;
    
    MovieDatabase& database_;
    IDataLoader& loader_;
    IDataExporter& exporter_;
    
    std::mutex queueMutex;
    std::condition_variable condition;
    bool stopping;
};

#endif