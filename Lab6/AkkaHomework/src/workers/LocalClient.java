package workers;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.StopMeMessage;

public class LocalClient extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String product;
    private int asks;

    public LocalClient(String product, int asks){
        this.asks = asks - 1;
        this.product = product;
        getSelf().tell(product, getSelf());
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    if(getSender()!=null && getSelf().path().equals(getSender().path())){
                        context().actorOf(Props.create(PriceComparer.class)).tell(product, getSelf());
                    }
                    else {
                        System.out.println(getSelf().path().name() + " returned: " + s + ", left " + asks);
                        if(0<asks--){
                            context().actorOf(Props.create(PriceComparer.class)).tell(product, getSelf());
                        }
                        else {
                            System.out.println(getSelf().path().name() + " end");
                            context().parent().tell(new StopMeMessage(), getSelf());
                        }
                    }
                })
                .match(Float.class, s -> {
                    System.out.println(getSelf().path().name() + " returned: " + s + ", left " + asks);
                    if(0<asks--){
                        context().actorOf(Props.create(PriceComparer.class)).tell(product, getSelf());
                    }
                    else {
                        System.out.println(getSelf().path().name() + " end");
                        context().parent().tell(new StopMeMessage(), getSelf());
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