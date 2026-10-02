package br.com.zenon;

import java.util.Optional;

public interface TransactionRepositoryInterface {
    Optional<Transaction> findByOriginName(String name);
}
