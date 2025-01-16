package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    } // end constructor

    public Message addMessage(Message message) throws Exception {
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            // check if message text if empty or longer than 225 characters long
            return null; // return null if message text is invalid
        } // end if statement 
        if (accountRepository.findById(message.getPostedBy()).get() == null) {
            // check if message is posted by a valid account holder
            return null; // if there is no valid account holder return null
        } // end if statement

        try {
            return messageRepository.save(message);
        } // end try block
        catch (Exception e) {
            throw e;
        } // end catch block
    } // end addMessage()
}
