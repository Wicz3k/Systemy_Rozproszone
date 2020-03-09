package server;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

public class ServerWorker implements Runnable {
    private ServerClient client;
    private HashMap<String, ServerClient> clientsData;

    public ServerWorker(ServerClient client, HashMap<String, ServerClient> clientsData){
        this.client = client;
        this.clientsData = clientsData;
    }

    @Override
    public void run() {
        try {
            if (!client.dataAvailable()){
                return;
            }
            String data = client.readTCP();
            System.out.println(data);
            if(data.toLowerCase().equals("close")){
                client.closeConnection(true);
                clientsData.remove(client.getName());
            }
            String[] splitedData = data.split(":", 2);
            if(splitedData.length == 2 && splitedData[1].length()>0) {
                String name = splitedData[0];
                if(clientsData.containsKey(name)){
                    ServerClient destinationClient = clientsData.get(name);
                    String message = client.getName() + ":" + splitedData[1];
                    destinationClient.sendTCP(message);
                }
                else{
                    client.sendTCP("server:User is not available");
                }
            }
            else{
                client.sendTCP("server:Bad message format");
            }
        }
        catch(IOException e){
            System.out.println("Connection problem");
            e.printStackTrace();
            client.closeConnection(true);
            clientsData.remove(client.getName());
        }
    }
}
