package br.com.zenon;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    public List<Transaction> readTransactions(String fileName) throws Exception {

        Path path = Path.of(fileName);

        try{
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::ParseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        }catch (Exception ex){
            throw new RuntimeException("Erro ao ler arquivo: " + fileName + ex);
        }

    }

    private Optional<Transaction> ParseTransaction(String line){
        try {
            String[] recordColumn = line.split(",");

            if (recordColumn[0] == null || recordColumn[0].isBlank()) throw new IllegalArgumentException("Step não pode ser nulo nem vazio");
            int step = Integer.parseInt(recordColumn[0]);

            if (recordColumn[1] == null || recordColumn[1].isBlank()) throw new IllegalArgumentException("Transaction Type não pode ser nulo nem vazio");
            TransactionType type = TransactionType.valueOf(recordColumn[1]);

            if (recordColumn[2] == null || recordColumn[2].isBlank()) throw new IllegalArgumentException("Valor de ammount não pode ser nulo nem vazio");
            BigDecimal amount = new BigDecimal(recordColumn[2]);

            if (recordColumn[3] == null || recordColumn[3].isBlank()) throw new IllegalArgumentException("Nome de origem não pode ser nulo nem vazio");
            if (recordColumn[4] == null || recordColumn[4].isBlank()) throw new NumberFormatException("Saldo antigo de origem não pode ser nulo nem vazio");
            if (recordColumn[5] == null || recordColumn[5].isBlank()) throw new NumberFormatException("Saldo novo de origem não pode ser nulo nem vazio");
            var origin = new TransactionCustomer(recordColumn[3], new BigDecimal(recordColumn[4]),
                    new BigDecimal(recordColumn[5]));

            if (recordColumn[6] == null || recordColumn[6].isBlank()) throw new IllegalArgumentException("Nome de destino não pode ser nulo nem vazio");
            if (recordColumn[7] == null || recordColumn[7].isBlank()) throw new NumberFormatException("Saldo antigo de destino não pode ser nulo nem vazio");
            if (recordColumn[8] == null || recordColumn[8].isBlank()) throw new NumberFormatException("Saldo novo de destino não pode ser nulo nem vazio");
            var recipient = new TransactionCustomer(recordColumn[6], new BigDecimal(recordColumn[7]),
                            new BigDecimal(recordColumn[8]));

            if (recordColumn[9] == null || recordColumn[9].isBlank()) throw new IllegalArgumentException("isFraud não pode ser nulo nem vazio");
            boolean isFraud = "1".equals(recordColumn[9]);
            if (recordColumn[10] == null || recordColumn[10].isBlank()) throw new IllegalArgumentException("isFlaggedFraud não pode ser nulo nem vazio");
            boolean isFlaggdFraud = "1".equals(recordColumn[10]);

            return Optional.of(new Transaction( step, type, amount, origin, recipient, isFraud, isFlaggdFraud));

        }catch (Exception ex){
            System.err.println("Erro: " + line + " | " + ex.getMessage());
            return Optional.empty();
        }
    }
}
