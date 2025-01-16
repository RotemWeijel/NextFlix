#ifndef I_THREAD_HANDLER_H
#define I_THREAD_HANDLER_H

// Interface for handling threads in the server
class IThreadHandler {
public:
   virtual ~IThreadHandler() = default;
   
   // Handle a new client connection
   // Takes the client socket descriptor as parameter
   virtual void handleConnection(int clientSocket) = 0;
};

#endif // I_THREAD_HANDLER_H