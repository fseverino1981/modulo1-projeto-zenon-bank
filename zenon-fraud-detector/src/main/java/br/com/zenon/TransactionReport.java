package br.com.zenon;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud){
    }

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {
        private static Statistics ZERO = new Statistics(0,0, BigDecimal.ZERO);
        private Statistics addReportTransaction(ReportTransaction rt) {
            return new Statistics(totalTransactions + 1,
                    totalFrauds + (rt.isFraud ? 1 : 0),
                    totalAmount.add(rt.amount));
        }
        private Statistics add(Statistics other){
            return new Statistics(
                    totalTransactions + other.totalTransactions,
                    totalFrauds + other.totalFrauds,
                    totalAmount.add(other.totalAmount));
        }
    }

    public Statistics generateReport(String fileName){
        Path path = Path.of(fileName);
        try (Stream<String> lines = Files.lines(path);){
            return lines
                .skip(1)
                    .map(this::parseReportTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            Statistics.ZERO,
                            Statistics::addReportTransaction,
                            Statistics::add);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Optional<ReportTransaction> parseReportTransaction(String line){
        String[] recordColumn = line.split(",");
        if (recordColumn[2] == null || recordColumn[2].isBlank()) throw new IllegalArgumentException("Valor de ammount não pode ser nulo nem vazio");
        BigDecimal amount = new BigDecimal(recordColumn[2]);
        if (recordColumn[9] == null || recordColumn[9].isBlank()) throw new IllegalArgumentException("isFraud não pode ser nulo nem vazio");
        boolean isFraud = "1".equals(recordColumn[9]);

        return Optional.of(new ReportTransaction(amount, isFraud));
    }

}
