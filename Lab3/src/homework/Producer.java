package homework;

import com.rabbitmq.client.*;
import com.rabbitmq.client.Consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static String producerName;
    public static void main(String[] argv) throws Exception {
        System.out.println("PRODUCER");

        producerName = readName();

        Channel channel = createChannel();

        Consumer messagesHandler = createConsumer(channel);

        String callbackQueueName = configureJobsQuery(channel, messagesHandler);

        configureAdminQueues(channel, messagesHandler);

        sendJobs(channel, callbackQueueName);
    }

    private static String readName() throws IOException {
        String producerName = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter producer name: ");
        producerName = br.readLine();
        return producerName;
    }

    private static Channel createChannel() throws IOException, TimeoutException {
        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    private static String configureJobsQuery(Channel channel, Consumer messagesHandler) throws IOException {
        // exchange
        channel.exchangeDeclare(Common.JOBS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String callbackQueueName = channel.queueDeclare().getQueue();

        channel.queueBind(callbackQueueName, Common.JOBS_EXCHANGE_NAME, callbackQueueName);

        channel.basicConsume(callbackQueueName, messagesHandler);
        return callbackQueueName;
    }

    private static void configureAdminQueues(Channel channel, Consumer messagesHandler) throws IOException {
        // adminQueues
        String adminProducer_QueueName = channel.queueDeclare().getQueue();
        channel.queueBind(adminProducer_QueueName, Common.JOBS_EXCHANGE_NAME, Common.ADMIN_PRODUCER_QUEUE_NAME);
        String adminAll_QueueName = channel.queueDeclare().getQueue();
        channel.queueBind(adminAll_QueueName, Common.JOBS_EXCHANGE_NAME, Common.ADMIN_ALL_QUEUE_NAME);

        // admin messages consume
        channel.basicConsume(adminProducer_QueueName, messagesHandler);
        channel.basicConsume(adminAll_QueueName, messagesHandler);
    }

    private static Consumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received: " + message);
                }
            };
    }

    private static void sendJobs(Channel channel, String callbackQueueName) throws IOException {
        int cnt = 0;
        while (true) {

            // read msg
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter service number (available values: 1,2,3), or exit: ");
            String key = br.readLine();

            // break condition
            if ("exit".equals(key)) {
                break;
            }

            if(!(key.equals("1")||key.equals("2")||key.equals("3"))){
                continue;
            }

            String message = producerName + "-" + (cnt++) + "-" + key;
            sendMessage(channel, callbackQueueName, key, message);

        }
    }

    private static void sendMessage(Channel channel, String callbackQueueName, String key, String message) throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .replyTo(callbackQueueName)
                .appId("P-" + producerName)
                .messageId(message)
                .build();
        channel.basicPublish(Common.JOBS_EXCHANGE_NAME, key, props, message.getBytes("UTF-8"));
        System.out.println("Sent: " + message);
    }
}