#ifndef SOCKET_OUTPUT_H
#define SOCKET_OUTPUT_H

#include "IOutputHandler.h"
#include <sys/socket.h>
#include <string>

class SocketOutput : public IOutputHandler {

public:
    // Constructor
    explicit SocketOutput(int socket);

    // Destructor
    ~SocketOutput() override = default;

    void sendOutput(const std::string& message) const override;

private:
    int clientSocket;

};

#endif // SOCKET_OUTPUT_H