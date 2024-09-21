package com.assignment.kirana.service;

import com.assignment.kirana.model.ReportResponse;
import com.assignment.kirana.model.ReportResponseWMY;
import com.assignment.kirana.model.TransactionRequest;
import com.assignment.kirana.model.TransactionResponse;
import com.assignment.kirana.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<ReportResponse> getAllTransactions() {
        List<TransactionRequest> transactionRequests = transactionRepository.findAll();
        return transactionRequests.stream()
                .map(transaction -> {
                    ReportResponse response = new ReportResponse();
                    response.setTransactionId(transaction.getTransactionId());
                    response.setType(transaction.getType());
                    response.setAmount(transaction.getConvertedAmount());
                    response.setCurrency(transaction.getCurrency());
                    response.setDescription(transaction.getDescription());
                    response.setStatus(transaction.getStatus());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<ReportResponseWMY> getTransactionsByType(String type) {
        List<TransactionRequest> transactions = transactionRepository.findAll();

        Map<String, List<TransactionRequest>> groupedTransactions = new HashMap<>();
        DateTimeFormatter formatter = null;

        switch (type.toLowerCase()) {
            case "weekly":
                formatter = DateTimeFormatter.ofPattern("YYYY-'W'ww");
                break;
            case "monthly":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                break;
            case "yearly":
                formatter = DateTimeFormatter.ofPattern("yyyy");
                break;
            default:
                throw new IllegalArgumentException("Invalid report type. Use 'weekly', 'monthly', or 'yearly'.");
        }

        for (TransactionRequest transaction : transactions) {
            String period = transaction.getTimestamp().format(formatter);  // Grouping key
            groupedTransactions
                    .computeIfAbsent(period, k -> new ArrayList<>())
                    .add(transaction);
        }

        List<ReportResponseWMY> reportList = new ArrayList<>();
        for (Map.Entry<String, List<TransactionRequest>> entry : groupedTransactions.entrySet()) {
            String period = entry.getKey();
            List<TransactionRequest> groupedList = entry.getValue();

            // Calculate totals for debit, credit, and net flow
            double totalDebit = groupedList.stream()
                    .filter(tr -> tr.getType().equalsIgnoreCase("debit"))
                    .mapToDouble(TransactionRequest::getConvertedAmount)
                    .sum();

            double totalCredit = groupedList.stream()
                    .filter(tr -> tr.getType().equalsIgnoreCase("credit"))
                    .mapToDouble(TransactionRequest::getConvertedAmount)
                    .sum();

            double netFlow = totalCredit - totalDebit;

            List<TransactionResponse> transactionDetails = groupedList.stream()
                    .map(transaction -> {
                        TransactionResponse response = new TransactionResponse();
                        response.setTransactionId(transaction.getTransactionId());
                        response.setType(transaction.getType());
                        response.setAmount(transaction.getConvertedAmount());
                        response.setCurrency(transaction.getCurrency());
                        response.setDescription(transaction.getDescription());
                        response.setStatus(transaction.getStatus());
                        return response;
                    })
                    .collect(Collectors.toList());

            // Create the report response for this period
            ReportResponseWMY report = new ReportResponseWMY();
            report.setPeriod(period);
            report.setTotalDebit(totalDebit);
            report.setTotalCredit(totalCredit);
            report.setNetFlow(netFlow);
            report.setTransactions(transactionDetails);

            reportList.add(report);
        }

        return reportList;
    }

}
