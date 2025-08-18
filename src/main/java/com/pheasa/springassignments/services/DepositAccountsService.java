package com.pheasa.springassignments.services;

import com.pheasa.springassignments.entity.DepositAccounts;

import java.util.List;

public interface DepositAccountsService {

    DepositAccounts createdDepositAccount(DepositAccounts depositAccounts);
    DepositAccounts updatedDepositAccount(Long id, DepositAccounts depositAccounts);
    DepositAccounts deletedDepositAccount(Long id);
    DepositAccounts getDepositAccountById(Long id);
    List<DepositAccounts> getAllDepositAccounts();
    List<DepositAccounts> searchDepositAccounts(String keyword);

}
