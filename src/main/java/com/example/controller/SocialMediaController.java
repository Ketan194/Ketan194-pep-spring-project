package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService, 
                                 AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
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

}
