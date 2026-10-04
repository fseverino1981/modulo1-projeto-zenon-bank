package br.com.zenon;

import javax.swing.*;
import br.com.zenon.TransactionReport.Statistics;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    static void main()
    {
        IO.println("Report em pt_BR");
        printReport("pt_BR");
        IO.println("Report em Inglês");
        printReport("en");
    }

    public static void printReport(String region){

        Locale locale = "pt_BR".equals(region) ? Locale.of("pt", "BR") : Locale.US;
        ResourceBundle report = ResourceBundle.getBundle("report", locale);
        String labelLines = report.getString("report.toal_linhas");
        String labelFrauds = report.getString("report.report.total_fraudes");
        String labelValue = report.getString("report.report.valor_total");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));
        NumberFormat integerFormatter = NumberFormat.getIntegerInstance(locale);

        String fileName = "./data/PS_20174392719_1491204439457_log.csv";
        TransactionReport transactionReport = new TransactionReport();
        Statistics statistics = transactionReport.generateReport(fileName);
        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """.formatted(labelLines, integerFormatter.format(statistics.totalTransactions()),
                labelFrauds, integerFormatter.format(statistics.totalFrauds()),
                labelValue, currencyFormatter.format(statistics.totalAmount())));
    }
}
