package com.pheasa.springassignments.repository;

import com.pheasa.springassignments.entity.DepositAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepositAccountsRepository extends JpaRepository<DepositAccounts, Long> {

    List<DepositAccounts> findByBstatusTrue();

    @Query("SELECT d FROM DepositAccounts d WHERE " +
            "(d.depositAccountNumber LIKE %?1% OR d.depositAccountName LIKE %?1%) AND d.bstatus = true")
    List<DepositAccounts> searchActiveDepositAccountByKeyword(String keyword);

    @Query("SELECT MAX(d.depositAccountNumber) FROM DepositAccounts d")
    String findMaxDepositNumber();

}
