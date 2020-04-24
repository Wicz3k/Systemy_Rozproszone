package homework;

import com.rabbitmq.client.*;
import com.rabbitmq.client.Consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class AdminPanel {
    public static void main(String[] argv) throws Exception {
        System.out.println("ADMIN");

        Channel channel = CreateChannel();

        sendMessages(channel);
    }

    private static void sendMessages(Channel channel) throws IOException {
        while (true) {
            // read msg
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String key = getKey(br);
            System.out.println("Enter message: ");
            String message = br.readLine();

            // break condition
            if ("exit".equals(message)) {
                break;
            }

            // publish
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .appId("admin")
                    .build();
            channel.basicPublish(Common.JOBS_EXCHANGE_NAME, key, props, message.getBytes("UTF-8"));
            System.out.println("Sent: " + message);
        }
    }

    private static Channel CreateChannel() throws IOException, TimeoutException {
        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
        channel.exchangeDeclare(Common.JOBS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // adminQueues
        String admin_QueueName = channel.queueDeclare().getQueue();
        channel.queueBind(admin_QueueName, Common.JOBS_EXCHANGE_NAME, "#");

        Consumer messagesHandler = createConsumer(channel);

        channel.basicConsume(admin_QueueName, true, messagesHandler);

        return channel;
    }

    private static String getKey(BufferedReader br) throws IOException {
        String key = "";
        while(!(key.equals(Common.ADMIN_CONSUMER_QUEUE_NAME)||key.equals(Common.ADMIN_PRODUCER_QUEUE_NAME)||key.equals(Common.ADMIN_ALL_QUEUE_NAME))) {
            System.out.println("Enter destination(producer, consumer, all): ");
            key = br.readLine();
        }
        return key;
    }

    private static Consumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String appId = "";
                if(properties!=null && properties.getAppId()!=null){
                    appId = properties.getAppId();
                }
                System.out.println("Received: " + message + "\tfrom:" + appId);
            }
        };
    }
}
