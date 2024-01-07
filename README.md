Simple chat server and client application. The server application listens for incoming connections from clients and handles communication between them. The client application connects to the server and allows users to send and receive messages.

Server

The Server class is responsible for running the chat server. It listens for incoming connections on a specified port and creates a new ClientHandler thread for each connected client.

The ClientHandler class manages communication with a single client. It reads messages from the client, broadcasts them to other clients, and sends messages to the client.

The startServer() method starts the server by creating a ServerSocket and entering an infinite loop that waits for incoming connections.

The acceptClient() method accepts a new connection from a client and returns the connected socket.

The startClientHandler() method creates a new ClientHandler for the connected client and starts it in a new thread.

Client

The Client class represents a client in the chat application. It connects to the server, sends and receives messages, and handles connection closing.

The initializeStreams() method creates the input and output streams for the client and sends the client's username to the server.

The startChatting() method starts the process of listening for messages and sending messages. It creates a thread for listening for messages from the server and a thread for reading messages from the console and sending them to the server.

The listenForMessages() method continuously reads messages from the server and prints them to the console. It runs in a separate thread.

The sendMessages() method continuously reads messages from the console and sends them to the server. It runs in a separate thread.

The closeResources() method closes the client's resources, including the socket and I/O streams.


Running Application 

Start the Server

Open Terminal or Command Line:

On Windows, you can use Command Prompt or PowerShell.
On macOS or Linux, just open the Terminal.

Navigate to the Project Directory:

Use the cd command to navigate to the directory containing your compiled .class files.
For example: cd path/to/your/project/directory

Run the Server:

Execute the Server class using the Java command: java Server
The server will start and wait for client connections.


Connect Clients

Open a New Terminal or Command Line Window for Each Client:

Each client must run in its own terminal window.


Navigate to the Project Directory (for each client window):

Just like with the server, navigate to the project directory: cd path/to/your/project/directory

Start a Client Instance:

Run the client using the command: java Client
You will be prompted to enter a username. This username will be used in the chat.

Chatting:

Once connected, you can start typing messages. Hit Enter to send each message.
Messages from other clients will appear in real-time.



References

Gyawali, Amit. "Multi Client Chat Server using Sockets and Threads in Java: Part 1" YouTube, uploaded by AMIT Gyawalil, 26 December 2020, https://.www.youtube.com/watch?v=9UpraaGqvSI

Tutorials, Neil. "Java Sockets - Sending Serialized Objects/Classes Networking" YouTube, uploaded by Neils Tutorials, 8 June 2019, https://www.youtube.com/watch?v=a-5Rb1_A4ew.

NeuralNine. "Simple TCP Chat Room in Java" YouTube, uploaded by NueralNine, 16 November 2021, https://www.youtube.com/watch?v=a-5Rb1_A4ew.




