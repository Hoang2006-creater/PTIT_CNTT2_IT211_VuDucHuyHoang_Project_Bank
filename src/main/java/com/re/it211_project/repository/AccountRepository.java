package com.re.it211_project.repository;


import com.re.it211_project.model.dto.request.ChangePinRequest;
import com.re.it211_project.model.dto.response.AccountResponse;
import com.re.it211_project.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber  );
    Optional<Account> findByAccountNumber(String accountNumber);
}