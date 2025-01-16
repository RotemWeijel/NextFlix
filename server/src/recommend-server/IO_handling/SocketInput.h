#ifndef SOCKET_INPUT_H
#define SOCKET_INPUT_H

#include "IInputHandler.h"
#include <sys/socket.h>
#include <string>

class SocketInput : public IInputHandler {

public:
    // Constructor
    explicit SocketInput(int socket);
    
    // Destructor
    ~SocketInput() override = default;

    std::string getInput() const override;

private:
    int clientSocket;
        
};

#endif // SOCKET_INPUT_H