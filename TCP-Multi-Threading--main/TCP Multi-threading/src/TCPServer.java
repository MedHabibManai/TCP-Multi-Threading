import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    public static void main(String[] args) {
        final int PORT = 12345;
        final int MAX_CLIENTS = 10;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur TCP démarré sur le port " + PORT);

            ExecutorService executorService = Executors.newFixedThreadPool(MAX_CLIENTS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Client says: " + inputLine);

                //string inverted
                StringBuilder reversed = new StringBuilder(inputLine).reverse();
                String response = reversed.toString();

                //delay
                Thread.sleep(2000);

                
                writer.println(response);
            }

            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
