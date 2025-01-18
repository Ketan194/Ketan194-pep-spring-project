package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

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

    /**
     * This function returns a List of messages in the database.
     * 
     * In Spring Data JPA `findAll()` cannot/does not return null ever, if the database is empty then an empty List is 
     *      returned instead. 
     * Therefore no error or exception handling needs to be done for this function.
     * 
     * @return List<Message> of all the messages in the database.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    } // end getAllMessages()

    /**
     * This function looks for a message in the database with a provided message_id and returns it if found. 
     * 
     * In Spring Data JPA `findById()` returns an optional containing the object that was found (or not found).
     * So after checking that an object was found it is safe to return it, if no object was found then return null.
     * 
     * @param id int that represents the message_id
     * @return The found message or null.
     */
    public Message getMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);

        if (!message.isPresent()) { // if nothing is present in the message optional then return null.
            return null;
        } // end if statement 

        return message.get(); // return the message that was found.
    } // end getMessageById()

    public boolean deleteMessageById(Integer id) {
        if (messageRepository.existsById(id)) { // if a message with a matching message_id exists 
            messageRepository.deleteById(id); // delete the message from the database
            return true; // return true
        } // end if statement

        return false; // if there is no matching message in the database return false
    } // end deleteMessageById()

    public boolean patchMessage(Integer id, String text) {
        // System.out.println(text);
        if (text.isEmpty() || text.length() > 255) {
            return false;
        } // end if statement 

        Optional<Message> message = messageRepository.findById(id);

        if (!message.isPresent()) { // if nothing is present in the message optional then return null.
            return false;
        } // end if statement 

        message.get().setMessageText(text);
        messageRepository.save(message.get());
        return true;
    } // end patchMesage()

    public List<Message> getByAccountId(Integer id) {
        return messageRepository.findMessagesByPostedBy(id);
    }
}
