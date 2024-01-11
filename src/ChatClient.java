import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Replace with the server's IP address
        int portNumber = 12345; // Replace with the server's port number

        try {
            Socket socket = new Socket(serverAddress, portNumber);

            // Create input and output streams
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientOutput = new PrintWriter(socket.getOutputStream(), true);

            // Create a separate thread to handle messages from the server
            Thread serverHandler = new Thread(() -> {
                try {
                    while (true) {
                        String serverMessage = serverInput.readLine();
                        if (serverMessage == null || serverMessage.equals("exit")) {
                            System.out.println("Disconnected from server");
                            break;
                        }
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverHandler.start();

            // Send messages to the server
            try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    //System.out.print("Client: ");
                    String clientMessage = userInput.readLine();
                    if (clientMessage.equals("exit")) {
                        break;
                    }
                    clientOutput.println(clientMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Close resources
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}