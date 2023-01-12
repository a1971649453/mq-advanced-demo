package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        String routingKey = "simple";

        String message = "hello, spring amqp!";
        Message build = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();

        // 自定义消息ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("","simple.queue", build,correlationData);
    }


    /**
     * 发送消息到死信队列
     * @throws InterruptedException
     */
    @Test
    public void testSendMessage2TTLQueue() throws InterruptedException {
        String message = "hello, spring amqp!";
        Message build = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();

        // 自定义消息ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("ttl.direct","ttl", build,correlationData);
        System.out.println("发送消息时间: " + LocalDateTime.now());
    }

    /**
     * 发送消息到延迟队列
     * @throws InterruptedException
     */
    @Test
    public void testSendMessage2DelayQueue() throws InterruptedException {
        String message = "hello, spring amqp!";
        Message build = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                // 10s后发送消息
                .setHeader("x-delay", 10000)
                .build();

        // 自定义消息ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("delay.direct","delay", build,correlationData);
        System.out.println("发送消息时间: " + LocalDateTime.now());
    }


    /**
     * 发送消息到延迟队列
     * @throws InterruptedException
     */
    @Test
    public void testSendMessage2LazyQueue() throws InterruptedException {
        String message = "hello, spring amqp!";
;


        long l = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            // 自定义消息ID
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("","lazy.queue", "lazy消息" + i,correlationData);
        }


        System.out.println("发送消息消耗时间: " + (System.currentTimeMillis() - l));

    }


}
