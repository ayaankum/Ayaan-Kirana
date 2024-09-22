package com.assignment.kirana.controller;

import com.assignment.kirana.model.ReportResponse;
import com.assignment.kirana.model.ReportResponseWMY;
import com.assignment.kirana.service.ReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {
  @Autowired private ReportService reportService;

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<ReportResponse>> getAllTransactions() {
    List<ReportResponse> transactions = reportService.getAllTransactions();
    return new ResponseEntity<>(transactions, HttpStatus.OK);
  }

  @GetMapping("/{periodType}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<ReportResponseWMY>> getReportByPeriod(
      @PathVariable String periodType) {
    List<ReportResponseWMY> report = reportService.getTransactionsByType(periodType);
    return new ResponseEntity<>(report, HttpStatus.OK);
  }
}
