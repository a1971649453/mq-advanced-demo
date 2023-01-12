package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public DirectExchange errorMessageExchange(){
        return new DirectExchange("error.direct");
    }
    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue", true);
    }
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
    }

    // 声明普通的 simple.queue队列，并且为其指定死信交换机：dl.direct
    @Bean
    public Queue simpleQueue2(){
        // 指定队列名称，并持久化
        return QueueBuilder.durable("simple.queue")
                // 指定死信交换机
                .deadLetterExchange("dl.direct")
//                //指定死信路由键
                .deadLetterRoutingKey("dl")
                .build();
    }
    // 声明死信交换机 dl.direct
    @Bean
    public DirectExchange dlExchange(){
        return new DirectExchange("dl.direct", true, false);
    }
    // 声明存储死信的队列 dl.queue
    @Bean
    public Queue dlQueue(){
        return new Queue("dl.queue", true);
    }
    // 将死信队列 与 死信交换机绑定
    @Bean
    public Binding dlBinding(){
        return BindingBuilder.bind(dlQueue()).to(dlExchange()).with("dl");
    }



    @Bean
    public Queue ttlQueue2(){
        // 指定队列名称，并持久化
        return QueueBuilder.durable("ttl.queue")
                // 指定死信交换机
                .deadLetterExchange("dl.direct")
//                //指定死信路由键
                .deadLetterRoutingKey("dl")
                // 设置10s 超时时间
                .ttl(10000)
                .build();
    }

    // 声明死信交换机 dl.direct
    @Bean
    public DirectExchange ttlExchange(){
        return new DirectExchange("ttl.direct", true, false);
    }


    // 将死信队列 与 死信交换机绑定
    @Bean
    public Binding ttlBinding(){
        return BindingBuilder.bind(ttlQueue2()).to(ttlExchange()).with("ttl");
    }


}
