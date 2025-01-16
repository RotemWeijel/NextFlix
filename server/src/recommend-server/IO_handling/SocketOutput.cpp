#include "SocketOutput.h"
#include <unistd.h>
#include <stdexcept>
#include <cstring>
#include <sys/socket.h>

SocketOutput::SocketOutput(int socket) : clientSocket(socket) {
    if (socket < 0) {
        throw std::runtime_error("Invalid socket descriptor");
    }
}

void SocketOutput::sendOutput(const std::string& message) const {
    std::string output = message;
    
    size_t totalSent = 0;
    while (totalSent < output.length()) {
        ssize_t sent = send(clientSocket, 
                           output.c_str() + totalSent, 
                           output.length() - totalSent, 
                           MSG_NOSIGNAL);
        
        if (sent < 0) {
            throw std::runtime_error("Failed to send message: " + 
                                   std::string(strerror(errno)));
        }
        totalSent += sent;
    }
}