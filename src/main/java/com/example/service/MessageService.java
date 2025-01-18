package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
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

    /**
     * Adds a message to the message database. 
     * 
     * The message can only be added if the messageText is not blank, is not over 255 characters, and postedBy refers to a real,
     *      existing user.
     * If the above conditions are met then the message can be added to the user. 
     * 
     * @param message The message that needs to be added to the database. 
     * @return The message that was added to the database. 
     * @throws Exception
     */
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
            return messageRepository.save(message); // try adding the message to the database
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

    /**
     * Will edit a message in the database. 
     * 
     * The message text can only be changed if the new message text if is not empty and not longer than 255 characters long. 
     * 
     * @param id The id of the message that needs to be changed. 
     * @param text The text that needs to replace the old message. 
     * @return True if the message was successfully changed and false otherwise.
     */
    public boolean patchMessage(Integer id, String text) {
        // System.out.println("Text value = ----------" + text + "--------  Id = " + id); // used for debugging

        if (text.isEmpty() || text.length() > 255) { // check if text is empty or longer than 255 characters long. 
            return false; // if text is bad return false
        } // end if statement

        Optional<Message> message = messageRepository.findById(id); // find message based on its id

        if (!message.isPresent()) { // if nothing is present in the message optional then return null.
            return false;
        } // end if statement 

        message.get().setMessageText(text); // change the text in the message
        messageRepository.save(message.get()); // add the message to the database
        return true; // if the message was added then return true
    } // end patchMesage()

    /**
     * Get a list of messages that were posted by a single user. 
     * 
     * @param id The id of the account whose messages need to be found. 
     * @return The List of messages that were posted by the user. 
     */
    public List<Message> getByAccountId(Integer id) {
        return messageRepository.findMessagesByPostedBy(id); // return the list of messages posted by the user
    } // end getByAccountId()
} // end MessageService Class