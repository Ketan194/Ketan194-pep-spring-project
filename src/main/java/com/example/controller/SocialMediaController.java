package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    } // end constructor

    /**
     * A handler for a POST request made to '/regiser'
     * 
     * In the request body is an Account that needs to be added to the database. 
     * If the username is already in use a 409 Conflict status code is returned. 
     * If the Account cannot be added for another reason a 400 Client Error is returned.
     * If the Account is added then the added Account is returned. 
     * 
     * The function returns a ResponseEntity so anything that is returned above is done through the ResponseEntity. 
     * 
     * @param account The Account that needs to be added to the database. 
     * @return A ResponseEntity that contains a status code and information about the results of attempting to add the account
     *         to the database. 
     */
     @PostMapping("/register")
     public ResponseEntity register(@RequestBody Account account) {
        try {
            Account added = accountService.register(account); // Try to add the account to the database.

            if (added == null) { // check if the account was added.
                return ResponseEntity.status(409).body("Conflict");              
                // if the account wasn't added it is likley cause of a conflict so return 409  
            } // end if statement 
        } // end try block
        catch (Exception e) {
            // if an exception is thrown when trying to add the account then return 400 along with what the execption is.
            return ResponseEntity.status(400).body("Client Error" + e.getMessage()); 
        } // end catch block
        return ResponseEntity.status(200).body(account); 
    } // end register handler

    /**
     * A handler for a POST request made to '/login'
     * 
     * In the request body is an Account that needs to login. 
     * If the login is successful then a status code of 200 is returned along with the account that is found in the database.
     * If the login is unsuccessful then a status code of 401 is returned along with a "Unauthorized" message.
     * 
     * The function returns a ResponseEntity so anything that is returned above is done through the ResponseEntity. 
     * 
     * @param account The Account that needs to login.
     * @return A ResponseEntity that contains a status code and information about the results of attempting to login.
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        Account foundAccount = accountService.login(account); // find an account with a matching username and password.

        if (foundAccount == null) { // if no account is found then return 401
            return ResponseEntity.status(401).body("Unauthorized");
        } // end if statement 
        else { // if an account is found return it along with 200
            return ResponseEntity.status(200).body(foundAccount);
        } // end else statement 
    } // end login handler 

    /**
     * A handler for a POST request made to '/messages'
     * 
     * This handler recives a Message that needs to be added to the message database. 
     * 
     * @param message Message that needs to be added to database.
     * @return The message that was added to the database along with 200 status code, or status code 400 if message wasn't added. 
     */
    @PostMapping("/messages")
    public ResponseEntity addMessage(@RequestBody Message message) {
        try {
            Message addedMessage = messageService.addMessage(message); // try adding the message to the database. 

            if (addedMessage == null) { // if message was not added return 400
                return ResponseEntity.status(400).body("Client Error");
            } // end if statement 

            return ResponseEntity.status(200).body(addedMessage); // return the added message along with 200.
        } // end try block
        catch (Exception e) {
            // If an exception is thrown then return 400 along with the exception message. 
            return ResponseEntity.status(400).body("Client Error" + e.getMessage());
        } // end catch block
    } // end addMessage() handler

    /**
     * A handler for a GET request made to '/messages'.
     * 
     * This handler will return a List of Messages that includes all the messages that are in the database. 
     * 
     * @return A List of Messages from the database along with a status code of 200.
     */
    @GetMapping("/messages")
    public ResponseEntity getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
        // if the database is empty then `messageService.getAllMessages()` will return an empty List (not null).
        // Thus it is safe to feed the result of `messageService.getAllMessages()` into the body of the ResponseEntity
    } // end getAllMessages() handler

    /**
     * A handler for a GET request made to '/messages/{messageId}'.
     * 
     * The handler will get a single message based on its message_id which is included in the path. 
     * 
     * @param messageId The message id to look for in the database. 
     * @return A ResponseEntity contianing the found message along with staus code 200. 
     */
    @GetMapping("messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable("messageId") Integer messageId) {
        Message found = messageService.getMessageById(messageId);

        if (found == null) {
            return ResponseEntity.status(200).body("");   
        } // end if statement 
        
        return ResponseEntity.status(200).body(found);
    } // end getMessageById handler

    /**
     * A handler for a DELETE request made to '/messages/{messageId}'.
     * 
     * The handler will delete a message from the database based on its message_id.
     * 
     * @param messageId The id of the message that needs to be deleted.
     * @return A ResponseEntity with staus code 200 along with how many rows were effected by the request. 
     */
    @DeleteMapping("messages/{messageId}") 
    public ResponseEntity deleteMessageById(@PathVariable("messageId") Integer messageId) {
        if (messageService.deleteMessageById(messageId)) { // if the delete happens successfully then return 200 and 1
            return ResponseEntity.status(200).body("1");
        } // end if statement

        return ResponseEntity.status(200).body(""); // if the delete does not happen return 200 and nothing
    } // end deleteMessageById handler

    /**
     * A handler for a PATCH request made to 'messages/{messageId}'.
     * 
     * The hadler will replace the message_text for a message that is found using its message id.
     * 
     * @param id The id of the message that needs to be changed.
     * @param messageText The text that needs to replace the old text of the message. 
     * @return A ResponseEntity with status code 200 along with how many rows were effected by the request, or 
     *      status code 400 if there was no effect.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity patchMessage(@PathVariable("messageId") Integer id, @RequestBody String messageText) {
        String text = messageText.substring(17, messageText.length() - 2);

        if (messageService.patchMessage(id, text)) { // if the patch happens successfully then return 200 and 1
            return ResponseEntity.status(200).body("1");
        } // end if statement

        return ResponseEntity.status(400).body("Client Error"); // if the patch does not happen return 200 and nothing
    } // end patchMessage handler 

    /**
     * A handler for a GET request made to '/accounts/{accountId}/messages'.
     * 
     * Finds all the messages made by a user and returns them as a List of Messages.
     * 
     * @param id The id of the account whose messages need to be retived. 
     * @return A List of Messages that were made by the user along with a status code of 200.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getByAccountId(@PathVariable("accountId") Integer id) {
        return ResponseEntity.status(200).body(messageService.getByAccountId(id));
    } // end getByAccountId handler
} // end SocialMediaController Class
