package com.example.service;

import org.hibernate.jdbc.Expectations;
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

    public Account register(Account account) throws Exception {
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
    }
}
