package com.pheasa.springassignments.services.impl;

import com.pheasa.springassignments.entity.DepositAccounts;
import com.pheasa.springassignments.repository.DepositAccountsRepository;
import com.pheasa.springassignments.services.DepositAccountsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DepositAccountsImpl implements DepositAccountsService {

    private final DepositAccountsRepository depositAccountsRepository;

    public DepositAccountsImpl(DepositAccountsRepository depositAccountsRepository) {
        this.depositAccountsRepository = depositAccountsRepository;
    }

    @Override
    public DepositAccounts createdDepositAccount(DepositAccounts depositAccounts) {
        if (depositAccounts.getDepositAccountNumber() == null || depositAccounts.getDepositAccountNumber().isEmpty()) {
            String maxDepositNumber = depositAccountsRepository.findMaxDepositNumber();
            int nextcode = 1;
            if (maxDepositNumber != null) {
                nextcode = Integer.parseInt(maxDepositNumber) + 1;
            }
            depositAccounts.setDepositAccountNumber(String.format("%09d", nextcode));
        }
        depositAccounts.setBstatus(true); // set active by default
        depositAccounts.setCreatedAt(LocalDateTime.now());
        depositAccounts.setUpdatedAt(LocalDateTime.now());
        return depositAccountsRepository.save(depositAccounts);
    }

    @Override
    public DepositAccounts updatedDepositAccount(Long id, DepositAccounts existingDepositAccounts) {
        Optional<DepositAccounts> optionalDepositAccounts = depositAccountsRepository.findById(id);

        if (optionalDepositAccounts.isPresent()) {
            DepositAccounts depositAccounts = optionalDepositAccounts.get();

            depositAccounts.setDepositAccountName(existingDepositAccounts.getDepositAccountName());
            depositAccounts.setAccountType(existingDepositAccounts.getAccountType()); // fixed here
            depositAccounts.setUpdatedAt(LocalDateTime.now());

            return depositAccountsRepository.save(depositAccounts);
        } else {
            throw new RuntimeException("Deposit Account not found with id " + id);
        }
    }

    @Override
    public List<DepositAccounts> getAllDepositAccounts() {
        return depositAccountsRepository.findByBstatusTrue();
    }

    @Override
    public DepositAccounts deletedDepositAccount(Long id) {
        DepositAccounts depositAccounts = depositAccountsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposit Account not found"));

        depositAccounts.setBstatus(false);
        depositAccounts.setUpdatedAt(LocalDateTime.now());

        return depositAccountsRepository.save(depositAccounts);
    }

    @Override
    public List<DepositAccounts> searchDepositAccounts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return depositAccountsRepository.findByBstatusTrue();
        }
        return depositAccountsRepository.searchActiveDepositAccountByKeyword(keyword);
    }

    @Override
    public DepositAccounts getDepositAccountById(Long id) {
        return depositAccountsRepository.findById(id).orElse(null);
    }
}
