package com.assignment.kirana.service;

import com.assignment.kirana.model.ReportResponse;
import com.assignment.kirana.model.ReportResponseWMY;
import com.assignment.kirana.model.TransactionRequest;
import com.assignment.kirana.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTransactions_returnsAllTransactions() {
        TransactionRequest request1 = new TransactionRequest();
        request1.setTransactionId("1");
        request1.setType("purchase");
        request1.setConvertedAmount(100.0);
        request1.setCurrency("USD");
        request1.setDescription("Test transaction 1");
        request1.setStatus("SUCCESS");

        TransactionRequest request2 = new TransactionRequest();
        request2.setTransactionId("2");
        request2.setType("refund");
        request2.setConvertedAmount(50.0);
        request2.setCurrency("EUR");
        request2.setDescription("Test transaction 2");
        request2.setStatus("SUCCESS");

        List<TransactionRequest> requests = Arrays.asList(request1, request2);
        when(transactionRepository.findAll()).thenReturn(requests);

        List<ReportResponse> responses = reportService.getAllTransactions();

        assertEquals(2, responses.size());
        assertEquals("1", responses.get(0).getTransactionId());
        assertEquals("2", responses.get(1).getTransactionId());
    }

    @Test
    void getTransactionsByType_weeklyReport() {
        TransactionRequest request1 = new TransactionRequest();
        request1.setTransactionId("1");
        request1.setType("debit");
        request1.setConvertedAmount(100.0);
        request1.setTimestamp(LocalDateTime.now().minusDays(1));

        TransactionRequest request2 = new TransactionRequest();
        request2.setTransactionId("2");
        request2.setType("credit");
        request2.setConvertedAmount(200.0);
        request2.setTimestamp(LocalDateTime.now().minusDays(2));

        List<TransactionRequest> requests = Arrays.asList(request1, request2);
        when(transactionRepository.findAll()).thenReturn(requests);

        List<ReportResponseWMY> report = reportService.getTransactionsByType("weekly");

        assertEquals(1, report.size());
        assertEquals(200.0, report.get(0).getTotalCredit());
        assertEquals(100.0, report.get(0).getTotalDebit());
        assertEquals(100.0, report.get(0).getNetFlow());
    }

    @Test
    void getTransactionsByType_monthlyReport() {
        TransactionRequest request1 = new TransactionRequest();
        request1.setTransactionId("1");
        request1.setType("debit");
        request1.setConvertedAmount(100.0);
        request1.setTimestamp(LocalDateTime.now().minusDays(10));

        TransactionRequest request2 = new TransactionRequest();
        request2.setTransactionId("2");
        request2.setType("credit");
        request2.setConvertedAmount(200.0);
        request2.setTimestamp(LocalDateTime.now().minusDays(20));

        List<TransactionRequest> requests = Arrays.asList(request1, request2);
        when(transactionRepository.findAll()).thenReturn(requests);

        List<ReportResponseWMY> report = reportService.getTransactionsByType("monthly");

        assertEquals(1, report.size());
        assertEquals(200.0, report.get(0).getTotalCredit());
        assertEquals(100.0, report.get(0).getTotalDebit());
        assertEquals(100.0, report.get(0).getNetFlow());
    }

    @Test
    void getTransactionsByType_yearlyReport() {
        TransactionRequest request1 = new TransactionRequest();
        request1.setTransactionId("1");
        request1.setType("debit");
        request1.setConvertedAmount(100.0);
        request1.setTimestamp(LocalDateTime.now().minusMonths(1));

        TransactionRequest request2 = new TransactionRequest();
        request2.setTransactionId("2");
        request2.setType("credit");
        request2.setConvertedAmount(200.0);
        request2.setTimestamp(LocalDateTime.now().minusMonths(2));

        List<TransactionRequest> requests = Arrays.asList(request1, request2);
        when(transactionRepository.findAll()).thenReturn(requests);

        List<ReportResponseWMY> report = reportService.getTransactionsByType("yearly");

        assertEquals(1, report.size());
        assertEquals(200.0, report.get(0).getTotalCredit());
        assertEquals(100.0, report.get(0).getTotalDebit());
        assertEquals(100.0, report.get(0).getNetFlow());
    }

    @Test
    void getTransactionsByType_invalidType() {
        try {
            reportService.getTransactionsByType("daily");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid report type. Use 'weekly', 'monthly', or 'yearly'.", e.getMessage());
        }
    }
}