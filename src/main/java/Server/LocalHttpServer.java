package Server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LocalHttpServer {

    public static void startServer() throws IOException {
        // Create HTTP server on localhost:8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Set up a handler for the /verify endpoint
        server.createContext("/verify", new VerifyTokenHandler());

        // Start the server
        server.start();
        System.out.println("Server started at http://localhost:8000/verify?token=...");
    }

    // Handler for the /verify endpoint
    static class VerifyTokenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();  // Get the query string (e.g., token=abcd1234)

            if (query != null && query.contains("token=")) {
                String token = query.split("=")[1];

                // Here you can check the token, validate it, and verify the user's email
                // For example:
                if (verifyToken(token)) {
                    String response = "Token is valid! User verified.";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    String response = "Invalid token.";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }

        // Method to simulate token verification
        private boolean verifyToken(String token) {
            // Simulate token verification logic (use a stored token from your database)
            // For now, we'll accept any token as valid
            return token != null && !token.isEmpty();
        }
    }


}
