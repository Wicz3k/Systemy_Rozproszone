import server.ChatServer;

import java.io.IOException;

public class ChatServerStarter {
    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer(9010);
        server.StartServer();
    }
}
