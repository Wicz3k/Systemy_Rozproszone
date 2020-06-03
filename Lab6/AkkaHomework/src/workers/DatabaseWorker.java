package workers;

import Database.Database;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import common.DBQueryMessage;
import common.StopMeMessage;
import common.WakeUpMessage;

import java.util.Random;

public class DatabaseWorker extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m -> {
                    Database db = Homework.Homework.db;
                    int asks = db.getAsks(m);
                    DBQueryMessage qm = new DBQueryMessage(asks + 1);
                    getSender().tell(qm, getSelf());
                    if(asks > 0 || !db.insert(m)){
                        db.update(m);
                    }
                    if(!getSender().isTerminated()) {
                        getSender().tell(new StopMeMessage(), getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message: type: " + o.getClass() + " value: " + o.toString()))
                .build();
    }
}