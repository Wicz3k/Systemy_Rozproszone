package workers;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.WakeUpMessage;

import java.util.Random;

public class PriceChecker extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef sender = null;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    sender = getSender();
                    Random rand = new Random();
                    int time = rand.nextInt(400)+100;
                    context().actorOf(Props.create(WakeUpWorker.class), "wake").tell(time, getSelf());
                    //throw new Exception();
                })
                .match(WakeUpMessage.class, f-> {
                    context().stop(getSender());
                    Random rand = new Random();
                    float result = rand.nextFloat()*9+1;
                    if(sender != null && !sender.isTerminated()) {
                        sender.tell(result, getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
}
