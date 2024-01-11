import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public static void main(String[] args) {
        int portNumber = 12345; // Replace with your desired port number
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server listening on port " + portNumber);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Create input and output streams
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream(), true);

            // Create a separate thread to handle messages from the client
            Thread clientHandler = new Thread(() -> {
                try {
                    while (true) {
                        String clientMessage = clientInput.readLine();
                        if (clientMessage == null || clientMessage.equals("exit")) {
                            System.out.println("Client disconnected");
                            break;
                        }
                        System.out.println("Client: " + clientMessage);

                        // Process the message or broadcast to other clients
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientHandler.start();

            // Send messages to the client
            try (BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                   // System.out.print("Server: ");
                    String serverMessage = serverInput.readLine();
                    if (serverMessage.equals("exit")) {
                        break;
                    }
                    serverOutput.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Close resources
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
