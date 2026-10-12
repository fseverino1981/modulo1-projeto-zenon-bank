package br.com.zenon;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class DBMain {

    static void main() {
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository();


        long startTimeSQL = System.nanoTime();
        List<Transaction> transactions =
                transactionIngestor.readTransactions("./data/PS_20174392719_1491204439457_log.csv");

        IO.println("Iniciando Inserts na Base de Dados");

        transactions
                .forEach(transactionSQLRepository::save);
        IO.println("Inserts Finalizados");
        long endTimeSQL = System.nanoTime();
        IO.println("Tempo de inclusão dos dados: "+ Duration.ofNanos(endTimeSQL - startTimeSQL).toSeconds());

        IO.println("Iniciando Busca de dado válido");
        long startTimeFind = System.nanoTime();
        Optional<Transaction> valido = transactionSQLRepository.findByOriginName("C1231006815");
        valido.ifPresentOrElse(
                t-> IO.println("Nome " + t.origin().name() + " encontrado"),
                () -> IO.println("Nome não encontrado"));
        IO.println("Fim de busca de dado válido: " + Duration.ofNanos(System.nanoTime() - startTimeFind).toMillis());

        IO.println("Iniciando Busca de dado NÃO válido");
        startTimeFind = System.nanoTime();
        Optional<Transaction> nValido = transactionSQLRepository.findByOriginName("C12345");

        nValido.ifPresentOrElse(
                t-> IO.println("Nome " + t.origin().name() + " encontrado"),
                () -> IO.println("Nome não encontrado"));
        IO.println("Fim de busca de dado NÃO válido: " + Duration.ofNanos(System.nanoTime() - startTimeFind).toMillis());


    }

}
