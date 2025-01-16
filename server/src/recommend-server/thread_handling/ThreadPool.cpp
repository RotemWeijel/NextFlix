#include "ThreadPool.h"
#include "../IO_handling/SocketInput.h"
#include "../IO_handling/SocketOutput.h"
#include "../main_body/MovieDatabase.h"
#include "../main_body/ClientHandler.h"
#include <unistd.h>

ThreadPool::ThreadPool(size_t numThreads, MovieDatabase& database, 
                     IDataLoader& loader, IDataExporter& exporter)
   : database_(database)
   , loader_(loader)
   , exporter_(exporter)
   , stopping(false) {
   
   // Create worker threads
   for(size_t i = 0; i < numThreads; ++i) {
       workers.emplace_back([this] { workerFunction(); });
   }
}

// Make sure to clean up threads when destroying the pool
ThreadPool::~ThreadPool() {
    stop();  
}

void ThreadPool::handleConnection(int clientSocket) {
   {
       std::unique_lock<std::mutex> lock(queueMutex);  // Lock queue
       tasks.push(clientSocket);                       // Add new task
   }
   condition.notify_one();  // Wake up one waiting worker
}

void ThreadPool::workerFunction() {
   while(true) {
       int clientSocket;
       {
           std::unique_lock<std::mutex> lock(queueMutex);
           
           // Wait until there's work or shutdown signal
           condition.wait(lock, [this] { 
               return stopping || !tasks.empty(); 
           });
           
           // Exit if shutting down and no tasks left
           if(stopping && tasks.empty()) {
               return;
           }
           
           // Get next task
           clientSocket = tasks.front();
           tasks.pop();
       }
       
       handleClient(clientSocket);  // Process the client
   }
}

void ThreadPool::stop() {
   {
       std::unique_lock<std::mutex> lock(queueMutex);
       stopping = true;  // Signal threads to exit
   }
   condition.notify_all();  // Wake all threads
   
   // Wait for all threads to finish
   for(auto& worker : workers) {
       if(worker.joinable()) {
           worker.join();
       }
   }
}

void ThreadPool::handleClient(int clientSocket) {
   try {
       // Setup socket I/O
       SocketInput sockinput(clientSocket);
       SocketOutput sockoutput(clientSocket);
       
       // Create and run client handler
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
   catch(const std::exception& e) {
       std::cerr << "Error handling client: " << e.what() << std::endl;
   }
   
   close(clientSocket);  // Clean up socket
}