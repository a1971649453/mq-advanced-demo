package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommonConfig implements ApplicationContextAware {


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // ioc容器中拿到rabbitTemplate对象
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

        // 设置发送者确认回调函数
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 自定义数据
             * @param b ack是否成功 确认成功为true
             * @param s 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                log.info("发送回调确认触发,消息ID ==> {}", correlationData.getId());
                if (b){
                    log.info("消息成功发送到交换机");
                }else {
                    log.error("消息发送到交换机失败,原因 ==> {}", s);
                }
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要这个方法触发 代表消息没能路由到队列 被mq返回
             * @param message 返回的消息
             * @param i 回复状态码
             * @param s 回复内容
             * @param s1 交换机
             * @param s2 路由key
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.info("消息发送失败,应答码{},原因{},交换机{},路由键{},消息{}",
                        i,s,s1,message.toString());
            }
        });
    }


//    @Bean
//    public DirectExchange simpleExchange(){
//        // 三个参数 交换机名称 是否持久化 是否自动删除
//        return new DirectExchange("simple.direct",true,false);
//    }
//
//    @Bean
//    public Queue simpleQueue(){
//        return new Queue("simple.queue");
//    }
//
//    @Bean
//    public Binding binding(){
//        return BindingBuilder.bind(simpleQueue()).to(simpleExchange()).with("simple");
//
//    }

    @Bean
    public Queue lazyQueue(){
        return QueueBuilder.durable("lazy.queue")
                .lazy()
                .build();
    }
    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable("normal.queue")
                .lazy()
                .build();
    }



}
