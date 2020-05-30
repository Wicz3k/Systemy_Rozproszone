package workers;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.WakeUpMessage;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class WakeUpWorker  extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, i -> {
                    ActorRef current = getSelf();
                    ActorRef sender = getSender();
                    Runnable callback = ()->{
                        if(!sender.isTerminated()){
                            sender.tell(new WakeUpMessage(), current);
                        }};
                    ExecutionContext exContext = context().dispatcher();
                    context().system().scheduler().scheduleOnce(Duration.create(i, TimeUnit.MILLISECONDS), callback, exContext);
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
}