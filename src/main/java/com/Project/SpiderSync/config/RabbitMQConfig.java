package com.Project.SpiderSync.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${crawler.queue.name:crawl-queue}")
    private String queueName;

    @Value("${crawler.queue.exchange:crawl-exchange}")
    private String exchangeName;

    @Value("${crawler.queue.routing-key:crawl.url}")
    private String routingKey;

    @Bean
    public Queue crawlQueue() {
        return new Queue(queueName, true); // durable queue
    }

    @Bean
    public TopicExchange crawlExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue crawlQueue, TopicExchange crawlExchange) {
        return BindingBuilder
            .bind(crawlQueue)
            .to(crawlExchange)
            .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
