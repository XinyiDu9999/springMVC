package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MailMessageListener {

    @Autowired ObjectMapper objectMapper;
    final Logger logger = LoggerFactory.getLogger(getClass());
    @JmsListener(destination = "/queue/mail")
    public void receiveMessage(String message) throws JsonProcessingException {
        Message recievedMessage = objectMapper.readValue(message, Message.class);
        logger.info("Received message: " + message);
        // 处理消息的业务逻辑
    }
}
