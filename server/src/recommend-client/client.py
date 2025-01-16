import socket
import sys

def connect_with_retry(server_ip, server_port, max_retries=3):
    for attempt in range(max_retries):
        try:
            client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            client_socket.connect((server_ip, server_port))
            # print(f"Connected to server {server_ip}:{server_port}")
            return client_socket
        except ConnectionRefusedError:
            if attempt < max_retries - 1:
                print(f"Connection attempt {attempt + 1} failed. Retrying in 1 second...")
                time.sleep(1)
            else:
                print(f"Failed to connect after {max_retries} attempts")
                raise
        except Exception as e:
            print(f"Failed to connect: {str(e)}")
            raise

def main():
    # Check if correct number of arguments are provided
    if len(sys.argv) != 3:
        print("Usage: python client.py <server_ip> <server_port>")
        sys.exit(1)
    
    # Parse server IP and port from command line arguments
    server_ip = sys.argv[1]
    try:
        server_port = int(sys.argv[2])
        if not (0 <= server_port <= 65535):
            raise ValueError("Port number must be between 0 and 65535.")
    except ValueError as e:
        print(f"Invalid port: {e}")
        sys.exit(1)
    
    try:
        # Create TCP socket
        client_socket = connect_with_retry(server_ip, server_port)
        
        # Main interaction loop
        while True:
            try:
                # Get user input
                user_command = input("")
                
                # Check for exit condition
                if user_command.lower() == 'exit':
                    client_socket.send('exit\n'.encode('utf-8'))  # Notify server
                    break
                
                # Send command to server
                client_socket.send((user_command + '\n').encode('utf-8'))
                
                # Receive response with proper buffering
                response = ""
                while True:
                    chunk = client_socket.recv(4096).decode('utf-8')
                    if not chunk:
                        break
                    response += chunk
                    if response.endswith('\n'):  # Check for message completion
                        break
                
                if response:
                    print(response.rstrip())  # rstrip to remove trailing newlines
            
            except Exception as e:
                print(f"Error during communication: {e}")
                break
    
    except ConnectionRefusedError:
        print(f"Could not connect to server at {server_ip}:{server_port}")
        sys.exit(1)
    
    except Exception as e:
        print(f"An error occurred: {e}")
        sys.exit(1)
    
    finally:
        if 'client_socket' in locals():
            client_socket.close()
            print("Connection closed.")

if __name__ == "__main__":
    main()
