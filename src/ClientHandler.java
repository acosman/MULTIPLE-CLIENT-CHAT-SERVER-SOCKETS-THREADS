import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ClientHandler class is responsible for managing communication between a server and a client.
 * It handles reading and sending messages to and from a connected client.
 */
public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    /**
     * Creates a ClientHandler for handling messages from a single client.
     *
     * @param socket The socket representing the connection to the client.
     */
    public ClientHandler(Socket socket) {
        initializeClientHandler(socket);
    }

    /**
     * Initializes the ClientHandler by setting up streams for communication and reading the client's username.
     * Adds this ClientHandler to the list of active client handlers and broadcasts an entry message.
     *
     * @param socket The socket representing the connection to the client.
     */
    private void initializeClientHandler(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat");
        } catch (IOException e) {
            closeResources();
        }
    }

    /**
     * The main run method executed by the thread, listening for messages from the client.
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            processClientMessages();
        }
    }

    /**
     * Processes incoming messages from the client.
     * Reads messages and broadcasts them to other clients.
     */
    private void processClientMessages() {
        try {
            String messageFromClient = bufferedReader.readLine();
            if (messageFromClient == null) {
                throw new IOException("Client disconnected");
            }
            broadcastMessage(clientUsername + ": " + messageFromClient);
        } catch (IOException e) {
            closeResources();
            Thread.currentThread().interrupt(); // Optional: to stop the current thread.
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     *
     * @param message The message to be broadcasted.
     */
    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.clientUsername.equals(clientUsername)) {
                sendMessageToClientHandler(clientHandler, message);
            }
        }
    }

    /**
     * Sends a message to a specific ClientHandler.
     *
     * @param clientHandler The ClientHandler to send the message to.
     * @param message       The message to be sent.
     */
    private void sendMessageToClientHandler(ClientHandler clientHandler, String message) {
        try {
            clientHandler.bufferedWriter.write(message);
            clientHandler.bufferedWriter.newLine();
            clientHandler.bufferedWriter.flush();
        } catch (IOException e) {
            closeResources();
        }
    }

    /**
     * Removes this client handler from the list of active handlers and notifies others of the disconnection.
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    /**
     * Closes the resources associated with this client handler, including input/output streams and socket.
     */
    public void closeResources() {
        removeClientHandler();
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
