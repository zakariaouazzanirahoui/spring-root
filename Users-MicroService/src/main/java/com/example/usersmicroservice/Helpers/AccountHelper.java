package com.example.usersmicroservice.Helpers;

import com.example.usersmicroservice.DTO.AccountDTO;
import com.example.usersmicroservice.Models.Account;

public class AccountHelper {
    public static AccountDTO convertToDTO(Account account) {
        return new AccountDTO(account.getId(), account.getFirst_name(), account.getLast_name(), account.getEmail(), account.getRole());
    }
}
