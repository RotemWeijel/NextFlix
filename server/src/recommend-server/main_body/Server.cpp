#include "../thread_handling/ThreadPool.h"
#include "../main_body/MovieDatabase.h"
#include "../data_handling/FileDataLoader.h"
#include "../data_handling/FileDataExporter.h"
#include <iostream>     // For std::cerr
#include <memory>      // For std::unique_ptr
#include <cstring>     // For strerror
#include <sys/socket.h> // For socket operations
#include <netinet/in.h> // For sockaddr_in
#include <unistd.h>    // For close
#include <arpa/inet.h> // For inet_ntop

class Server
{
private:
    int serverPort;
    int serverSocket;
    MovieDatabase &database;
    IDataLoader &fileLoader;
    IDataExporter &fileExporter;
    std::unique_ptr<IThreadHandler> threadHandler;
    const int MAX_PENDING_CONNECTIONS = 10;

    void acceptClients() {
        while (true) {
            struct sockaddr_in clientAddr;
            socklen_t clientAddrLen = sizeof(clientAddr);

            int clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clientAddrLen);
            
            if (clientSocket < 0) {
                std::cerr << "Server: Failed to accept client connection: " << strerror(errno) << std::endl;
                continue;
            }

            // Convert client IP to string for logging
            char clientIP[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &(clientAddr.sin_addr), clientIP, INET_ADDRSTRLEN);        
            
            try {
                threadHandler->handleConnection(clientSocket);
            }
            catch (const std::exception& e) {
                std::cerr << "Server: Failed to create thread: " << e.what() << std::endl;
                close(clientSocket);
            }
        }
    }

public:
    Server(int port, IDataLoader &loader, IDataExporter &exporter, MovieDatabase &database)
        : serverPort(port)
        , fileLoader(loader)
        , fileExporter(exporter)
        , database(database)
        , threadHandler(std::make_unique<ThreadPool>(5, database, loader, exporter))  {
        fileLoader.load(database);
    }
    
    ~Server() {
        close(serverSocket);
    }

    void setupServerSocket() {
        struct sockaddr_in serverAddr;
        
        if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
            throw std::runtime_error("Failed to create socket");
        }

        // Set socket options
        int opt = 1;
        if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
            close(serverSocket);
            throw std::runtime_error("Failed to set SO_REUSEADDR option");
        }

        if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEPORT, &opt, sizeof(opt)) < 0) {
            close(serverSocket);
            throw std::runtime_error("Failed to set SO_REUSEPORT option");
        }

        serverAddr.sin_family = AF_INET;
        serverAddr.sin_addr.s_addr = INADDR_ANY;
        serverAddr.sin_port = htons(serverPort);

        if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0) {
            close(serverSocket);
            throw std::runtime_error("Failed to bind socket: " + std::string(strerror(errno)));
        }

        if (listen(serverSocket, MAX_PENDING_CONNECTIONS) < 0) {
            close(serverSocket);
            throw std::runtime_error("Failed to listen on socket: " + std::string(strerror(errno)));
        }

        acceptClients();
    }

};

int main(int argc, char *argv[])
{
    int port_server = -1;
    if (argc > 1)
    {
        std::string port_arg = argv[1];
        try
        {
            port_server = std::stoi(port_arg);
        }
        catch (const std::invalid_argument &e)
        {
            // there is letter in the arg
            return 0;
        }
        catch (const std::out_of_range &e)
        {
            // the num is outofrange of int
            return 0;
        }
        if (port_server > 65535 || port_server < 0) // check validty of port for tcp protocol
        {
            return 0;
        }
    }
    if (port_server == -1)
    {
        return 0;
    }
    FileDataLoader fileLoader("../../data/");
    FileDataExporter fileExporter("../../data/");
    MovieDatabase database(fileLoader, fileExporter);
    Server server = Server(port_server, fileLoader, fileExporter, database);
    server.setupServerSocket();
    return 0;
}