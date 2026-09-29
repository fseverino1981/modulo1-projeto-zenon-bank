package br.com.zenon;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyser {

    private final List<Transaction> transactions;

    public FraudAnalyser(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public long countFrauds(){
        return getFraudStream()
                .count();
    }

    public List<BigDecimal> findHighestValueFrauds(int limit){
        return getHighValueFraudStream()
                .map(Transaction::amount)
                .limit(limit)
                .toList();
    }

    private @NotNull Stream<Transaction> getHighValueFraudStream() {
        return getFraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed());
    }

    private @NotNull Stream<Transaction> getFraudStream() {
        return transactions.stream()
                .filter(Transaction::isFraud);
    }

    public Set<String> findToSuspiciousClient(int limit){
        return getHighValueFraudStream()
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(limit)
                .collect(Collectors.toSet());
    }

    public BigDecimal calculateTotalFraudLoss(){
        return getFraudStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> countFraudByType(){
        return getFraudStream()
                .collect(
                        Collectors.groupingBy(
                                Transaction::type,
                                Collectors.counting(
                        )
                ));
    }

}
