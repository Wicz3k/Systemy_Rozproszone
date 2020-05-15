import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Z2_LocalActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m ->
                {
                    if (sender().path().root() == getSelf().path().root()) {
                        getContext().actorSelection("akka://remote_system@127.0.0.1:2552/user/remote")
                                .tell(m, getSelf());
                    }
                    else{
                        System.out.println(m);
                    }
                })
                 // TODO: forward msg from AppLocal to remote actor
                 // TODO: print response
                 // TODO: need to distinguish between remote response and msg from AppLocal to avoid infinite msg loop
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
