package com.pheasa.springassignments.controller;

import com.pheasa.springassignments.entity.DepositAccounts;
import com.pheasa.springassignments.services.DepositAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/deposit-accounts")
public class DepositAccountController {

    private final DepositAccountsService depositAccountsService;

    @Autowired
    public DepositAccountController (DepositAccountsService depositAccountsService){
        this.depositAccountsService = depositAccountsService;
    }

    @GetMapping
    public List<DepositAccounts> getAllDepositAccounts(){
        return depositAccountsService.getAllDepositAccounts();
    }

    @PostMapping
    public DepositAccounts registerDepositAccount(@RequestBody DepositAccounts depositAccounts){
        depositAccounts.setBstatus(true);
        depositAccounts.setCreatedAt(LocalDateTime.now());
        depositAccounts.setUpdatedAt(LocalDateTime.now());
        return depositAccountsService.createdDepositAccount(depositAccounts);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<DepositAccounts> updateDepositAccount(@PathVariable Long id, @RequestBody DepositAccounts depositAccounts){
        DepositAccounts updated = depositAccountsService.updatedDepositAccount(id, depositAccounts);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletedDepositAccount(@PathVariable Long id){
        depositAccountsService.deletedDepositAccount(id);
        return ResponseEntity.ok("Deposit has been deleted.");
    }

    @GetMapping("/search")
    public List<DepositAccounts> searchDepositAccounts(@RequestParam(required = false) String keyword){
        return depositAccountsService.searchDepositAccounts(keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositAccounts> getDepositAccountById(@PathVariable Long id){
        DepositAccounts depositAccount = depositAccountsService.getDepositAccountById(id);
        if(depositAccount == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(depositAccount);
    }
}
