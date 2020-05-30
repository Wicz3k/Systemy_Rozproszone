package workers;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import common.StopMeMessage;
import common.WakeUpMessage;
import org.w3c.dom.ls.LSOutput;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;

public class PriceComparer extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private float val = 0;
    private boolean isFinished = false;
    private ActorRef sender = null;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    sender = getSender();
                    context().actorOf(Props.create(WakeUpWorker.class), "wake").tell(300, getSelf());
                    context().actorOf(Props.create(PriceChecker.class), "comparer1").tell(s, getSelf());
                    context().actorOf(Props.create(PriceChecker.class),"comparer2").tell(s, getSelf());
                })
                .match(Float.class, f->{
                    context().stop(getSender());
                    if(!isFinished) {
                        if (val <= 0) {
                            val = f;
                        } else {
                            isFinished = true;
                            float res = Math.min(val, f);
                            sender.tell(res, null);
                            context().parent().tell(new StopMeMessage(), getSelf());
                        }
                    }
                })
                .match(WakeUpMessage.class, f-> {
                    context().stop(getSender());
                    if(!isFinished){
                        isFinished = true;
                        if(val<=0){
                            sender.tell("Could not get any response", null);
                        }
                        else{
                            sender.tell(val, null);
                        }
                        context().parent().tell(new StopMeMessage(), getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
    private static SupervisorStrategy strategy
            = new OneForOneStrategy(0, Duration.create(10, TimeUnit.MILLISECONDS), DeciderBuilder.
            matchAny(o -> restart()).
            build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}