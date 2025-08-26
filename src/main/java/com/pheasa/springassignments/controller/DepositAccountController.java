package com.pheasa.springassignments.controller;

import com.pheasa.springassignments.configuration.annotations.AuditFilter;
import com.pheasa.springassignments.configuration.annotations.MyRetryable;
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
    public DepositAccountController(DepositAccountsService depositAccountsService){
        this.depositAccountsService = depositAccountsService;
    }

    @AuditFilter
    @GetMapping
    public List<DepositAccounts> getAllDepositAccounts(){
        return depositAccountsService.getAllDepositAccounts();
    }

    @AuditFilter
    @PostMapping
    public DepositAccounts registerDepositAccount(@RequestBody DepositAccounts depositAccounts){
        depositAccounts.setBstatus(true);
        depositAccounts.setCreatedAt(LocalDateTime.now());
        depositAccounts.setUpdatedAt(LocalDateTime.now());
        return depositAccountsService.createdDepositAccount(depositAccounts);
    }

//    @AuditFilter
//    @GetMapping("/active")
//    public List<DepositAccounts> getActiveDepositAccounts(){
//        return depositAccountsService.getActiveDepositAccounts();
//    }

//    @AuditFilter
//    @GetMapping("/inactive")
//    public List<DepositAccounts> getInactiveDepositAccounts(){
//        return depositAccountsService.getInactiveDepositAccounts();
//    }

    @AuditFilter
    @PutMapping("/edit/{id}")
    public ResponseEntity<DepositAccounts> updateDepositAccount(@PathVariable Long id,
                                                                @RequestBody DepositAccounts depositAccounts){
        DepositAccounts updated = depositAccountsService.updatedDepositAccount(id, depositAccounts);
        return ResponseEntity.ok(updated);
    }

    @AuditFilter
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletedDepositAccount(@PathVariable Long id){
        depositAccountsService.deletedDepositAccount(id);
        return ResponseEntity.ok("Deposit has been deleted.");
    }

    @AuditFilter
    @MyRetryable(maxRetries = 3, retryDelay = 2000, retryFor = {RuntimeException.class})
    @GetMapping("/search")
    public List<DepositAccounts> searchDepositAccounts(@RequestParam(required = false) String keyword){
        return depositAccountsService.searchDepositAccounts(keyword);
    }

    @AuditFilter
    @MyRetryable(maxRetries = 3, retryDelay = 2000, retryFor = {RuntimeException.class})
    @GetMapping("/{id}")
    public ResponseEntity<DepositAccounts> getDepositAccountById(@PathVariable Long id){
        DepositAccounts depositAccount = depositAccountsService.getDepositAccountById(id);
        if(depositAccount == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(depositAccount);
    }
}
