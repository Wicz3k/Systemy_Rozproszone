import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import common.CreateActorMessage;
import workers.MessageConsumer;
import workers.Writer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Homework {
    public static void main(String[] args) throws Exception {

        // create actor system & actors
        final ActorSystem system = ActorSystem.create("local_system");
        final ActorRef actor = system.actorOf(Props.create(MessageConsumer.class), "mess");
        final ActorRef writer = system.actorOf(Props.create(Writer.class), "writer");
        System.out.println("Started. Command 'q' to close, 'cs' to create clients");

        // read line & send to actor
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
            if(line.equals("cs")){
                System.out.println("How many clients: ");
                line = br.readLine();
                int amount = 0;
                try{
                    amount = Integer.parseInt(line);
                }
                catch(Exception e){
                    System.out.println("This is not a number.");
                }
                System.out.println("How many asks: ");
                line = br.readLine();
                int asks = 0;
                try{
                    asks = Integer.parseInt(line);
                }
                catch(Exception e){
                    System.out.println("This is not a number.");
                }
                if(amount !=0){
                    System.out.println("Product: ");
                    String product = br.readLine();
                    actor.tell(new CreateActorMessage(amount, product, asks), null);
                }
            }
            else {
                actor.tell(line, writer);     // send message to actor
            }
        }

        // finish
        system.terminate();
    }
}
