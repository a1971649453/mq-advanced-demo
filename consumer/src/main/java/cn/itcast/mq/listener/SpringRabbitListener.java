package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class SpringRabbitListener {

//    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueue1(String msg) {
//        System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
//    }


//    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueue2(String msg) {
//        log.info("消费者接收到simple.queue的消息：【{}】", msg);
//        log.info("消费者消费了消息");
//        // 模拟异常
//        System.out.println(1 / 0);
//        log.debug("消息处理完成！");
//    }

//    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueue2(String msg, Channel channel, Message message) throws IOException {
//        log.info("消费者接收到simple.queue的消息：【{}】", msg);
//        log.info("消费者消费了消息");
//        // 模拟异常
////        System.out.println(1 / 0);
//        log.debug("消息处理完成！");
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue2(String msg, Channel channel, Message message) throws IOException {
        log.info("消费者接收到simple.queue的消息：【{}】 当前时间{}", msg, LocalDateTime.now());
        log.info("消费者消费了消息");
        // 模拟异常
        System.out.println(1 / 0);
        log.debug("消息处理完成！");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    /**
     * 监听死信队列
     * @param msg
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("dl.queue"),
            exchange = @Exchange(value = "dl.direct", type = "direct"),
            key = "dl"
    ))
    public void listenSimpleQueue2(String msg) throws IOException {

        System.out.println("收到死信队列的消息：【" + msg + "】");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("delay.queue"),
            exchange = @Exchange(value = "delay.direct", type = "direct",delayed = "true"),
            key = "delay"
    ))
    public void listenDelayedQueue2(String msg) throws IOException {

        System.out.println("收到延迟队列的消息：【" + msg + "】");
    }
}
