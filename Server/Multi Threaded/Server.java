import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        int port = 8010;

        // Thread pool with 10 threads
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Give client work to thread pool
                pool.execute(() -> {
                    try {
                        BufferedReader fromClient = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream())
                        );

                        PrintWriter toClient = new PrintWriter(
                            clientSocket.getOutputStream(), true
                        );

                        String message = fromClient.readLine();
                        System.out.println("Client says: " + message);

                        toClient.println("Hello from the server");

                        fromClient.close();
                        toClient.close();
                        clientSocket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
