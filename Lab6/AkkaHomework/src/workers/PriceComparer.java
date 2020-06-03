package workers;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import common.DBQueryMessage;
import common.StopMeMessage;
import common.WakeUpMessage;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.restart;

public class PriceComparer extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private float val = 0;
    private boolean isFinished = false;
    private ActorRef sender = null;
    private int asks = -1;
    private boolean both = false;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    sender = getSender();
                    context().actorOf(Props.create(WakeUpWorker.class), "wake").tell(300, getSelf());
                    context().actorOf(Props.create(PriceChecker.class), "comparer1").tell(s, getSelf());
                    context().actorOf(Props.create(PriceChecker.class),"comparer2").tell(s, getSelf());
                    context().actorOf(Props.create(DatabaseWorker.class),"database").tell(s, getSelf());
                })
                .match(Float.class, f->{
                    context().stop(getSender());
                    if(!isFinished) {
                        if (val <= 0) {
                            val = f;
                        } else if(asks < 0) {
                            val = Math.min(val, f);
                            both = true;
                        }
                        else{
                            makeResponse();
                        }
                    }
                })
                .match(DBQueryMessage.class, m ->{
                    if(!isFinished) {
                        this.asks = m.getAsks();
                        if(both){
                            makeResponse();
                        }
                    }
                })
                .match(StopMeMessage.class, m ->{
                    context().stop(getSender());
                })
                .match(WakeUpMessage.class, f-> {
                    context().stop(getSender());
                    if(!isFinished){
                        makeResponse();
                    }
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }

    private void makeResponse() {
        isFinished = true;
        String asksMess = String.valueOf(asks);
        if(asks<0){
            asksMess = "Could not get any response";
        }
        String priceMess = String.valueOf(val);
        if(val<=0){
            priceMess = "Could not get any response";
        }
        sender.tell("price: " + priceMess + " asks: " + asksMess, null);
        context().parent().tell(new StopMeMessage(), getSelf());
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