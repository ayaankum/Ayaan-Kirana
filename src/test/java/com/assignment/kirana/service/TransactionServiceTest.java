package com.assignment.kirana.service;


import com.assignment.kirana.model.TransactionRequest;
import com.assignment.kirana.model.TransactionResponse;
import com.assignment.kirana.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private ValueOperations<String, Double> valueOperations;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void createTransaction_successfulTransaction() {
        TransactionRequest request = new TransactionRequest();
        request.setCurrency("USD");
        request.setAmount(100.0);
        request.setType("purchase");
        request.setDescription("Test transaction");

        when(redisTemplate.opsForValue().get("USD_INR")).thenReturn(75.0);
        when(transactionRepository.save(any(TransactionRequest.class))).thenReturn(request);

        TransactionResponse response = transactionService.createTransaction(request);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals(100.0, response.getAmount());
        assertEquals("USD", response.getCurrency());
    }

    @Test
    void createTransaction_sameCurrency() {
        TransactionRequest request = new TransactionRequest();
        request.setCurrency("INR");
        request.setAmount(100.0);
        request.setType("purchase");
        request.setDescription("Test transaction");

        when(transactionRepository.save(any(TransactionRequest.class))).thenReturn(request);

        TransactionResponse response = transactionService.createTransaction(request);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals(100.0, response.getAmount());
        assertEquals("INR", response.getCurrency());
    }

    @Test
    void getExchangeRate_cachedRate() {
        when(redisTemplate.opsForValue().get("USD_INR")).thenReturn(75.0);

        double rate = transactionService.getExchangeRate("USD", "INR");

        assertEquals(75.0, rate);
    }

    @Test
    void getAllTransactions_returnsAllTransactions() {
        TransactionRequest request1 = new TransactionRequest();
        request1.setTransactionId("1");
        request1.setType("purchase");
        request1.setAmount(100.0);
        request1.setCurrency("USD");
        request1.setDescription("Test transaction 1");
        request1.setStatus("SUCCESS");

        TransactionRequest request2 = new TransactionRequest();
        request2.setTransactionId("2");
        request2.setType("refund");
        request2.setAmount(50.0);
        request2.setCurrency("EUR");
        request2.setDescription("Test transaction 2");
        request2.setStatus("SUCCESS");

        List<TransactionRequest> requests = Arrays.asList(request1, request2);
        when(transactionRepository.findAll()).thenReturn(requests);

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        assertEquals(2, responses.size());
        assertEquals("1", responses.get(0).getTransactionId());
        assertEquals("2", responses.get(1).getTransactionId());
    }
}