package Server;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import sr.grpc.gen.Channel;
import sr.grpc.gen.NameMessage;
import sr.grpc.gen.NameStreamGrpc;

import java.util.*;

public class NameStreamImpl extends NameStreamGrpc.NameStreamImplBase {
    Hashtable<Channel, List<StreamObserver<NameMessage>>> channelObservers = new Hashtable<>();
    Hashtable<Channel, Thread> channels = new Hashtable<>();

    public NameStreamImpl(){
        for (Channel ch: Channel.values()) {
            if(ch == Channel.UNRECOGNIZED){
                continue;
            }
            Thread channelThread = getStreamThread(ch);
            channelThread.start();
            channels.put(ch, channelThread);
            channelObservers.put(ch, new ArrayList<StreamObserver<NameMessage>>());
        }
    }

    private Thread getStreamThread(Channel ch) {
        return new Thread(() -> {
                    Random rand = new Random();
                    while(true) {
                        var message = NameMessage.newBuilder()
                                .setChannel(ch)
                                .setTemperature(rand.nextFloat() * 50 - 10)
                                .setNumberOfPeoples(rand.nextInt(10))
                                .addAllInformation(Arrays.asList("Adam, Sebastian, Michał"))
                                .build();
                        var observers = channelObservers.get(ch);
                        System.out.println(ch + " send message to " + observers.size() + " observers.");
                        for(int i= observers.size()-1; i>=0; i--){
                            try{
                                observers.get(i).onNext(message);
                            }
                            catch(StatusRuntimeException e){
                                observers.remove(i);
                                System.out.println("Lost connection observer on channel: " + ch);
                            }
                            catch(Exception e){
                                System.out.println("Exception on channel: " + ch);
                                System.out.println(e);
                            }
                        }
                        try {
                            Thread.sleep(rand.nextInt(3000) + 1000);
                        } catch (InterruptedException e) {
                            System.out.println("interupted");
                        }
                    }
                });
    }

    public void subscribeChannel(sr.grpc.gen.Task request,
                                 io.grpc.stub.StreamObserver<sr.grpc.gen.NameMessage> responseObserver) {
        Channel ch = request.getChannel();
        var list = channelObservers.get(ch);
        list.add(responseObserver);
        channelObservers.replace(ch, list);
    }

}
