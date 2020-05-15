import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Z2_RemoteActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                 .match(String.class, m -> {
                     System.out.println(m);
                     getContext().actorSelection(sender().path())
                         .tell(m.toUpperCase(), getSelf());
                 })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
