import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server class represents a simple TCP server.
 * It accepts client connections and handles each client in a separate thread.
 */
public class Server {
    private ServerSocket serverSocket;

    /**
     * Constructs a Server instance with a specific port.
     *
     * @param port The port number on which the server will listen for client connections.
     * @throws IOException If an I/O error occurs when opening the server socket.
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Starts the server, listening for client connections and handling them.
     * Runs an infinite loop that constantly listens for new client connections and starts a new thread for each.
     */
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = acceptClient();
                startClientHandler(clientSocket);
            }
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
     * Accepts a new client connection.
     * Waits for a client to connect and then returns the connected socket.
     *
     * @return The connected client socket.
     * @throws IOException If an I/O error occurs when waiting for a client connection.
     */
    private Socket acceptClient() throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("A new client has connected!");
        return socket;
    }

    /**
     * Starts a new thread to handle client communication.
     * Creates a ClientHandler for the connected client and starts it in a new thread.
     *
     * @param socket The socket of the connected client.
     */
    private void startClientHandler(Socket socket) {
        ClientHandler clientHandler = new ClientHandler(socket);
        new Thread(clientHandler).start();
    }

    /**
     * Handles IOExceptions that occur in the startServer method.
     * Logs the exception's message and stack trace to standard output.
     *
     * @param e The IOException that was caught.
     */
    private void handleIOException(IOException e) {
        System.out.println("An error occurred in startServer: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Closes the server socket.
     * Attempts to close the server socket if it is not null.
     */
    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to start the Server.
     * Creates an instance of Server and starts it.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(5678);
            server.startServer();
        } catch (IOException e) {
            System.out.println("Could not start the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
