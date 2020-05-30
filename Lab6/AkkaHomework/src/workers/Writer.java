package workers;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Writer extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    System.out.println("returned: " + s);
                })
                .match(Float.class, s -> {
                    System.out.println("returned: " + s);
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
}