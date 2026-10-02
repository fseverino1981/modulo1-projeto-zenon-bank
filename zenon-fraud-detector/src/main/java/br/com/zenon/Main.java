package br.com.zenon;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    static void main() {

//        Transaction transaction1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
//                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("170136.0")),
//                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
//                        false, false);
//
//        Transaction transaction2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
//                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
//                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
//                true, false);
//
//        IO.println("Transacão 1: " + transaction1);
//        IO.println("Transacão 2: " + transaction2);
        TransactionIngestor transactionIngestor = new TransactionIngestor();

        List<Transaction> transactions;

        transactions = transactionIngestor.readTransactions("./data/PS_20174392719_1491204439457_log.csv");
        /*System.out.println(transactions.size());

        transactions.stream().limit(10).forEach(System.out::println);

        List<Transaction> transactionsWithErrors = transactionIngestor.readTransactions("./data/paysim_with_bad_data.csv");
        System.out.println(transactionsWithErrors.size());

        transactionsWithErrors.forEach(IO::println);

        FraudAnalyser fraudAnalyser = new FraudAnalyser(transactions);

        var totalFrauds = fraudAnalyser.countFrauds();

        IO.println("1. Total de Fraudes: " + totalFrauds);

        var top3Frauds = fraudAnalyser.findHighestValueFrauds(3);

        IO.println("2. Top 3 Fraudes de Maior Valor:");

        top3Frauds.forEach(amount -> IO.println("%.2f".formatted(amount)));


        IO.println("3. Clientes Suspeitos");
        var suspiciousClient = fraudAnalyser.findToSuspiciousClient(5);
        suspiciousClient.forEach(IO::println);

        var totalFraudLoss = fraudAnalyser.calculateTotalFraudLoss();
        IO.println("4. Prejuizo Total: " + NumberFormat.getCurrencyInstance(Locale.of("pt","BR")).format(totalFraudLoss));

        IO.println("5. Fraudes por Tipo:");
        Map<TransactionType, Long> fraudsByType = fraudAnalyser.countFraudByType();
        fraudsByType.forEach((type, count) ->
                IO.println(" - %s: %d".formatted(type, count)));*/

        var transactionListRepository = new TransactionListRepository(transactions);

//        String nameToFind = "C12345";
//        extracted(transactionListRepository, nameToFind);
//        nameToFind = "C1231006815";
//        extracted(transactionListRepository, nameToFind);

        String nameToFind = "C1868032458";
        long startTime = System.nanoTime();
        transactionListRepository.findByOriginName(nameToFind)
                .ifPresentOrElse(IO::println,() ->
                        IO.println("Transação não encontrada para: " + nameToFind));
        System.out.println("Tempo de execucão: " + Duration.ofNanos(System.nanoTime() - startTime).toMillis());

        var transactionMapRepository = new TransactionMapRepository(transactions);
        startTime = System.nanoTime();
        transactionMapRepository.findByOriginName(nameToFind)
                .ifPresentOrElse(IO::println,() ->
                        IO.println("Transação não encontrada para o cliente: " + nameToFind));
        System.out.println("Tempo de execucão: " + Duration.ofNanos(System.nanoTime() - startTime).toMillis());
    }

}
