import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client class represents a client in a chat application.
 * It manages the connection to the server and handles sending and receiving messages.
 */
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    /**
     * Constructs a new Client instance and initializes the connection to the server.
     *
     * @param hostname The hostname of the chat server.
     * @param port     The port number of the chat server.
     * @param username The username of the client.
     * @throws IOException If an I/O error occurs while establishing a connection.
     */
    public Client(String hostname, int port, String username) throws IOException {
        socket = new Socket(hostname, port);
        this.username = username;
        initializeStreams();
    }

    /**
     * Initializes the input and output streams for the client.
     * Sends the client's username to the server as the first message.
     *
     * @throws IOException If an I/O error occurs when creating the streams.
     */
    private void initializeStreams() throws IOException {
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    /**
     * Starts the process of listening for messages and sending messages.
     */
    public void startChatting() {
        listenForMessages();
        sendMessages();
    }

    /**
     * Continuously listens for messages from the server and prints them to the console.
     * Runs in a separate thread.
     */
    private void listenForMessages() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    System.out.println(bufferedReader.readLine());
                } catch (IOException e) {
                    closeResources();
                    break;
                }
            }
        }).start();
    }

    /**
     * Continuously reads messages from the console and sends them to the server.
     */
    private void sendMessages() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeResources();
        }
    }

    /**
     * Closes the client's resources including the socket and I/O streams.
     */
    private void closeResources() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to start the Client.
     * Prompts for the user's username and connects to the chat server.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            System.out.println("Enter your username for the group chat: ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            Client client = new Client("localhost", 5678, username);
            client.startChatting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
