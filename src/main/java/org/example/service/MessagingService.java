package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.example.entity.Message;

@Service
public class MessagingService {
    @Autowired ObjectMapper objectMapper;
    @Autowired  JmsTemplate jmsTemplate;


    public void sendEmail(@Autowired Message message) throws JsonProcessingException {
        String str = objectMapper.writeValueAsString(message);
        jmsTemplate.send("/queue/mail", new MessageCreator() {
            @Override
            public jakarta.jms.Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(str);
            }
        });

    }


}
