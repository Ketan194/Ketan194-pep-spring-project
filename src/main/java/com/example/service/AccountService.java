package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    } // end constructor

    /**
     * Adds an Account to the account database. 
     * 
     * The account can only be added if the username is not empty, the password is at least 4 characters long, and 
     *      the username is not already in use. 
     * If the above conditions are met then the account can be added. 
     * 
     * @param account The Account that needs to be added to the database.
     * @return The account that was added to the database. 
     * @throws Exception
     */
    public Account register(Account account) throws Exception {
        if (account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            // check to make sure the username is not empty and the password is longer than 4 characters. 
            return null;
        } // end if statement 
        Account found = accountRepository.findAccountByUsername(account.getUsername());

        if (found != null) { // check if an account exists with a matching username
            return null; // if there is no account with a matching username then return null
        } // end if statement

        try {
            return accountRepository.save(account); // try to add the account to the database
        } // end try block
        catch (Exception e) {
            throw e; // throw execption if account is not added
        } // end catch block
    } // end register()

    /**
     * Logs a user into their account. 
     * 
     * @param account The account that is trying to log in. 
     * @return
     */
    public Account login(Account account) {
        Account found = accountRepository.findAccountByUsername(account.getUsername());
        
        if (found == null) { // check if an account exists in the database with a matching username
            return null; // if there is not account with a matching username return null
        } // end if statement

        if (account.getPassword().equals(found.getPassword())) { 
            // check if the password of the account matches the password of the account found in the database.
            return found; // return the account
        } // end if statement
        
        return null; // return null if the password does not match
    } // end login()
} // end AccountService Class
