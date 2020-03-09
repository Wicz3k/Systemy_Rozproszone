package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ScheduledFuture;

public class ServerClient {
    private String name;
    private Socket socketTCP;
    private PrintWriter out;
    private BufferedReader in;

    private ScheduledFuture<?> scheduled;

    public ServerClient(Socket socketTCP) throws IOException {
        this.socketTCP = socketTCP;
        out = new PrintWriter(socketTCP.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
        this.name = readTCP();
    }

    public void sendTCP(String message) {
        out.println(message);
    }

    public String readTCP() throws IOException {
        return in.readLine();
    }

    public void closeConnection(boolean initByClient) {
        try {
            if (!initByClient) {
                sendTCP("close");
            }
            socketTCP.close();
            socketTCP = null;
            scheduled.cancel(false);
        } catch (IOException e) {
            System.out.println("problem witch close socket");
        }
    }

    public boolean dataAvailable() throws IOException {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setScheduled(ScheduledFuture<?> scheduled){
        this.scheduled = scheduled;
    }
}
