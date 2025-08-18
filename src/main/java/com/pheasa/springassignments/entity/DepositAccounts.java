package com.pheasa.springassignments.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "opt_deposit_accounts")
@Getter
@Setter
public class DepositAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deposit_account_number",unique = true, nullable = false)
    private String depositAccountNumber;

    @Column(name = "deposit_account_name",nullable = false)
    private String depositAccountName;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "b_status", nullable = false)
    private boolean bstatus = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DepositAccounts(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public DepositAccounts(String depositAccountNumber, String depositAccountName, String accountType){
        this.depositAccountNumber = depositAccountNumber;
        this.depositAccountName = depositAccountName;
        this.accountType = accountType;
    }

    @PrePersist
    protected void onCreated(){
        createdAt = LocalDateTime.now();
    }

}
