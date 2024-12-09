package com.epam.training.ticketservice.core.account.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsernameAndPassword(String username, String password);

    Optional<Account> findByUsername(String userName);
}