package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ChatServer {
    private int portNumber;
    private ServerSocket serverSocket;
    private HashMap<String, ServerClient> clientsData;
    ScheduledThreadPoolExecutor executor;

    public ChatServer(int portNumber){
        this.portNumber = portNumber;
    }

    public void StartServer() throws IOException {
        try {
            clientsData = new HashMap<>();
            // create socket
            serverSocket = new ServerSocket(portNumber);
            executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(8);
            System.out.println("Server is ready");
            while(true){

                // accept client
                acceptClient(serverSocket.accept());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
            stopServer();
        }
    }

    private void acceptClient(Socket clientSocket) throws IOException {
        try {
            ServerClient client = new ServerClient(clientSocket);
            if(!clientsData.containsKey(client.getName()) && !client.getName().equals("server") && !client.getName().equals("")) {
                clientsData.put(client.getName(), client);
                ServerWorker worker = new ServerWorker(client, clientsData);
                client.setScheduled(executor.scheduleWithFixedDelay(worker, 10, 100, TimeUnit.MILLISECONDS));
                client.sendTCP("Accepted");
                System.out.println("client connected");
            }
            else{
                client.sendTCP("Rejected");
                System.out.println("client rejected");
                clientSocket.close();
            }
        }
        catch (IOException e) {
            System.out.println("IOException when try to read data from socket");
            clientSocket.close();
        }
    }

    private void stopServer(){
        for(var client : clientsData.values()){
            client.closeConnection(false);
        }
        clientsData.clear();
        System.out.println("Bye");
    }
}