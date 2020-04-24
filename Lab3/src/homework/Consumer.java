package homework;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Consumer {
    public static void main(String[] argv) throws Exception {
        System.out.println("CONSUMER");

        String consumerName = getConsumerName();

        // connection & channels
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        Channel channel2 = connection.createChannel();

        // exchange
        channel.exchangeDeclare(Common.JOBS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String service1 = getServiceName(1);
        String service2 = getServiceName(2);

        // queue & bind
        String queueName1 = configureServiceQueue(channel, service1);
        String queueName2 = configureServiceQueue(channel2, service2);

        // consumer (message handling)
        com.rabbitmq.client.Consumer consumer = createConsumerWithResponse(channel, consumerName);
        com.rabbitmq.client.Consumer consumer2 = createConsumerWithResponse(channel2, consumerName);

        // start listening
        System.out.println("Waiting for messages...");
        channel.basicConsume(queueName1, false, consumer);
        channel2.basicConsume(queueName2, false, consumer2);

        createAdminChannel(connection);
    }

    private static String configureServiceQueue(Channel channel, String serviceName) throws IOException {
        String queueName = Common.JOBS_EXCHANGE_NAME + "-" + serviceName;
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, Common.JOBS_EXCHANGE_NAME, serviceName);
        System.out.println("created queue: " + queueName);
        channel.basicQos(1);
        return queueName;
    }

    private static String getConsumerName() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter consumer name: ");
        return br.readLine();
    }

    private static String getServiceName(int number) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String serviceName = "";
        while (!(serviceName.equals("1") || serviceName.equals("2") || serviceName.equals("3"))) {
            System.out.println("Enter service "+ number + " (1,2,3): ");
            serviceName = br.readLine();
        }
        return serviceName;
    }

    private static void createAdminChannel(Connection connection) throws IOException {
        Channel channelAdmin = connection.createChannel();
        configureAdminQueues(channelAdmin);
    }

    private static void configureAdminQueues(Channel channelAdmin) throws IOException {

        //adminChannels
        String adminConsumer_QueueName = channelAdmin.queueDeclare().getQueue();
        channelAdmin.queueBind(adminConsumer_QueueName, Common.JOBS_EXCHANGE_NAME, Common.ADMIN_CONSUMER_QUEUE_NAME);
        String adminAll_QueueName = channelAdmin.queueDeclare().getQueue();
        channelAdmin.queueBind(adminAll_QueueName, Common.JOBS_EXCHANGE_NAME, Common.ADMIN_ALL_QUEUE_NAME);

        com.rabbitmq.client.Consumer messageConsumer = createConsumerWithoutResponse(channelAdmin);

        // start listening
        channelAdmin.basicConsume(adminConsumer_QueueName, false, messageConsumer);
        channelAdmin.basicConsume(adminAll_QueueName, false, messageConsumer);
    }

    private static com.rabbitmq.client.Consumer createConsumerWithResponse(Channel channel, String consumerName) {
        return new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received: " + message);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String response = "Done: " + message + " by " + consumerName;
                    if(properties!=null && properties.getReplyTo()!=null) {
                        AMQP.BasicProperties props = new AMQP.BasicProperties
                                .Builder()
                                .appId(consumerName)
                                .build();
                        channel.basicPublish("", properties.getReplyTo(), props, response.getBytes("UTF-8"));
                    }
                    System.out.println("End: " + message);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
    }

    private static com.rabbitmq.client.Consumer createConsumerWithoutResponse(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
            }
        };
    }
}
