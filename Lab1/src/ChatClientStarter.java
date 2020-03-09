import client.ChatClient;

import java.io.IOException;

public class ChatClientStarter {
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        client.startChat("localhost", 9010);
    }

}
