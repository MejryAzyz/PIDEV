package Server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import tools.MyDataBase;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        Connection cnx;

        public VerifyTokenHandler() {
            cnx = MyDataBase.getInstance().getCnx();
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery(); // Get the query string (e.g., token=abcd1234)

            if (query != null && query.contains("token=")) {
                String token = query.split("=")[1];

                // Check if token is valid
                if (verifyTokenInDatabase(token)) {
                    // Update user status
                    updateUserVerificationStatus(token);

                    String response = "Email verified successfully! You can now log in.";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    String response = "Invalid or expired token.";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }

        // Check if the token exists in the database
        private boolean verifyTokenInDatabase(String token) {
            try {
                String sql = "SELECT COUNT(*) FROM utilisateur WHERE verification_token = ?";
                PreparedStatement pstmt = cnx.prepareStatement(sql);
                pstmt.setString(1, token);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Update user status to verified
        private void updateUserVerificationStatus(String token) {
            try  {
                String sql = "UPDATE utilisateur SET verif = 1, verification_token = 'verified' WHERE verification_token = ?";
                PreparedStatement pstmt = cnx.prepareStatement(sql);
                pstmt.setString(1, token);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
