import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 8010;
    private static final int THREAD_POOL_SIZE = 10;

    public static void handleClient(Socket clientSocket) {
        try (
            Socket socket = clientSocket;
            BufferedReader fromClient = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String clientMessage = fromClient.readLine();

            System.out.println(
                "Client connected: " + socket.getRemoteSocketAddress()
            );
            System.out.println("Client says: " + clientMessage);

            toClient.println("Hello from the server");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is listening on port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                threadPool.submit(() -> handleClient(clientSocket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
