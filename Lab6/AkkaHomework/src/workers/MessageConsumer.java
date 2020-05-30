package workers;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.CreateActorMessage;
import common.StopMeMessage;

public class MessageConsumer extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    context().actorOf(Props.create(PriceComparer.class)).tell(s, getSender());
                })
                .match(CreateActorMessage.class, m->{
                    for(int i = 0; i<m.getAmount(); i++){
                        context().actorOf(Props.create(LocalClient.class, m.getProduct(), m.getAsks()));
                    }
                })
                .match(StopMeMessage.class, s ->{
                    if(!getSender().isTerminated()){
                        context().stop(getSender());
                    }
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
}
