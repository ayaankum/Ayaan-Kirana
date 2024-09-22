package com.assignment.kirana.model;

import java.util.List;

public class ReportResponseWMY {
  private String period; // Week, Month, or Year label
  private double totalDebit;
  private double totalCredit;
  private double netFlow;
  private List<TransactionResponse> transactions;

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public double getTotalDebit() {
    return totalDebit;
  }

  public void setTotalDebit(double totalDebit) {
    this.totalDebit = totalDebit;
  }

  public double getTotalCredit() {
    return totalCredit;
  }

  public void setTotalCredit(double totalCredit) {
    this.totalCredit = totalCredit;
  }

  public double getNetFlow() {
    return netFlow;
  }

  public void setNetFlow(double netFlow) {
    this.netFlow = netFlow;
  }

  public List<TransactionResponse> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<TransactionResponse> transactions) {
    this.transactions = transactions;
  }
}
