import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
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
        System.out.println("Started. Command 'q' to close");

        // read line & send to actor
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
            actor.tell(line, writer);     // send message to actor
        }

        // finish
        system.terminate();
    }
}
