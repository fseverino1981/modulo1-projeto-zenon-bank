package br.com.zenon;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> readTransactions(String fileName) throws Exception {

        Path path = Path.of(fileName);
        List<Transaction> transactionList = new ArrayList<>();

        try{
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::ParseTransaction)
                    .toList();

        }catch (Exception ex){
            throw new RuntimeException("Erro ao ler arquivo: " + fileName + ex);
        }

    }

    private Transaction ParseTransaction(String line){
        String[] recordColumn = line.split(",");

        return new Transaction(
                Integer.parseInt(recordColumn[0]),
                TransactionType.valueOf(recordColumn[1]),
                new BigDecimal(recordColumn[2]),
                new TransactionCustomer(recordColumn[3],
                        new BigDecimal(recordColumn[4]),
                        new BigDecimal(recordColumn[5])),
                new TransactionCustomer(recordColumn[6],
                        new BigDecimal(recordColumn[7]),
                        new BigDecimal(recordColumn[8])),
                Integer.parseInt(recordColumn[9]) == 1,
                Integer.parseInt(recordColumn[10]) == 1
        );
    }
}
