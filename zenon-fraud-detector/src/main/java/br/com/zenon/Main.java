package br.com.zenon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() throws Exception {

        /*Transaction transaction1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("170136.0")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                        false, false);

        Transaction transaction2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false);

        IO.println("Transacão 1: " + transaction1);
        IO.println("Transacão 2: " + transaction2);
*/
        TransactionIngestor transactionIngestor = new TransactionIngestor();
/*
        List<Transaction> transactions = new ArrayList<>();

        transactions = transactionIngestor.readTransactions("./data/PS_20174392719_1491204439457_log.csv");
        System.out.println(transactions.size());

        transactions.stream().limit(10).forEach(System.out::println);
*/
        List<Transaction> transactionsWithErrors = transactionIngestor.readTransactions("./data/paysim_with_bad_data.csv");
        System.out.println(transactionsWithErrors.size());

        transactionsWithErrors.forEach(IO::println);
    }

}
