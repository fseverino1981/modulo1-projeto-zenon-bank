package br.com.zenon;

import javax.swing.*;
import br.com.zenon.TransactionReport.Statistics;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    static void main()
    {
        IO.println("Report em PT/BR");
        printReport("pt/BR");
        IO.println("Report em Inglês");
        printReport("en");
    }

    public static void printReport(String region){

        Locale locale = "pt/BR".equals(region) ? Locale.of("pt", "BR") : Locale.US;
        ResourceBundle report = ResourceBundle.getBundle("report", locale);
        String labelLines = report.getString("report." + ReportLabel.TOTAL_LINHAS.toString().toLowerCase());
        String labelFrauds = report.getString("report." + ReportLabel.TOTAL_FRAUDES.toString().toLowerCase());
        String labelValue = report.getString("report." + ReportLabel.VALOR_TOTAL.toString().toLowerCase());
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);

        String fileName = "./data/PS_20174392719_1491204439457_log.csv";
        TransactionReport transactionReport = new TransactionReport();
        Statistics statistics = transactionReport.generateReport(fileName);
        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """.formatted(labelLines, numberFormatter.format(statistics.totalTransactions()),
                labelFrauds, numberFormatter.format(statistics.totalFrauds()),
                labelValue, currencyFormatter.format(statistics.totalAmount())));
    }
}
