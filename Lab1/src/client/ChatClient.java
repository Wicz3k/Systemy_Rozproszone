package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public void startChat(String hostName, int portNumber) throws IOException {
        Socket socket = null;
        try {
            // create socket
            socket = new Socket(hostName, portNumber);

            // in & out streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // send msg, read response
            System.out.println("Write your name:");
            Scanner input = new Scanner(System.in);
            String name = input.nextLine();
            out.println(name);
            String response = in.readLine();
            System.out.println("received response: " + response);
            if(response.toLowerCase().equals("accepted")){
                communication(out, in);
            }
            System.out.println("Bye");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }
    }

    private void communication(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Write your message with pattern: 'user:message'");
        Scanner input = new Scanner(System.in);
        InputStreamReader input2 = new InputStreamReader(System.in);
        boolean finish = false;
        while(!finish){
            if(input2.ready()){
                String message = input.nextLine();
                if(message.toLowerCase().equals("close")){
                    finish = true;
                }
                out.println(message);
            }
            if(in.ready()){
                String message = in.readLine();
                if(message.toLowerCase().equals("close")){
                    finish = true;
                }
                System.out.println(message);
            }
        }
    }
}


