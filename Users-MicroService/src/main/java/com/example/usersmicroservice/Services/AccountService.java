package com.example.usersmicroservice.Services;

import com.example.usersmicroservice.Models.Account;
import com.example.usersmicroservice.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerUser(Account account) {
        account.setPassword(account.getPassword());
        return accountRepository.save(account);
    }

    public Optional<Account> getUserByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
    public Optional<Account> getUserById( Long userId) {
        return accountRepository.findById(userId);
    }

    public boolean verifyPassword(String enteredPassword, Account user) {
        return BCrypt.checkpw(enteredPassword,user.getPassword());
    }



    public List<Account> getAllUsers(){
        return (List<Account>) accountRepository.findAll();
    }

    public List<Account> getUsersByIds(Set<Long> userIds) {
        // Convert Iterable<Account> to List<Account>
        return StreamSupport.stream(accountRepository.findAllById(userIds).spliterator(), false)
                .collect(Collectors.toList());
    }

}
