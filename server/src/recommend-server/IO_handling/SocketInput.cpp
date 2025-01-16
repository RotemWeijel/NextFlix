#include "SocketInput.h"
#include <unistd.h>
#include <stdexcept>
#include <cstring>
#include <sys/socket.h>

SocketInput::SocketInput(int socket) : clientSocket(socket) {
    if (socket < 0) {
        throw std::runtime_error("Invalid socket descriptor");
    }
}

std::string SocketInput::getInput() const {
    const int BUFFER_SIZE = 1024;
    char buffer[BUFFER_SIZE];
    std::string input;
    
    // Read until we get a newline or reach the end
    while (true) {
        ssize_t bytesRead = recv(clientSocket, buffer, BUFFER_SIZE - 1, 0);
        
        if (bytesRead < 0) {
            throw std::runtime_error("Failed to read from socket: " + std::string(strerror(errno)));
        }
        
        if (bytesRead == 0) {
            // Connection closed by client
            if (input.empty()) {
                throw std::runtime_error("Connection closed by client");
            }
            break;
        }
        
        buffer[bytesRead] = '\0';
        input += buffer;
        
        // Check if we've received a complete line
        if (input.find('\n') != std::string::npos) {
            break;
        }
    }
    
    // Remove trailing newline if present
    if (!input.empty() && input.back() == '\n') {
        input.pop_back();
    }
    
    return input;
}