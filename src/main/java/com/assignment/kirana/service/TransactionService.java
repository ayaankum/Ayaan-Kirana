package com.assignment.kirana.service;

import com.assignment.kirana.repository.TransactionRepository;
import com.assignment.kirana.model.TransactionRequest;
import com.assignment.kirana.model.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final Map<String, Double> rateCache = new HashMap<>();
    @Autowired
    private RedisTemplate<String, Double> redisTemplate;
    private static final long CACHE_EXPIRATION = 60 * 60;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private TransactionRepository transactionRepository;
    public TransactionResponse createTransaction(TransactionRequest request) {

        String currency = request.getCurrency();
        double originalAmount = request.getAmount();

        double exchangeRate = getExchangeRate(currency, "INR");
        double convertedAmt = originalAmount*exchangeRate;

        String transactionId = UUID.randomUUID().toString();
        request.setTransactionId(transactionId);
        request.setConvertedAmount(convertedAmt);
        request.setTimestamp(LocalDateTime.now());
        request.setStatus("SUCCESS");
        transactionRepository.save(request);

        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(request.getTransactionId());
        response.setType(request.getType());
        response.setAmount(request.getAmount());
        response.setCurrency(request.getCurrency());
        response.setDescription(request.getDescription());
        response.setStatus("SUCCESS");
        return response;
    }

    private double getExchangeRate(String fromCurrency, String toCurrency) {

        //if fromCurrency and toCurrency is same return 1 does not need to make api call
        if(fromCurrency.equals(toCurrency)) return 1.0;

        //implemented rate caching
        String cacheKey = fromCurrency + "_" + toCurrency;
        Double cachedRate = redisTemplate.opsForValue().get(cacheKey);
        if (cachedRate != null) {
            return cachedRate;
        }
//        if (rateCache.containsKey(cacheKey)) {
//            return rateCache.get(cacheKey);
//        }

        String url = "https://api.fxratesapi.com/latest";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("base", fromCurrency);

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(uriBuilder.toUriString(), Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double exchangeRate = rates.getOrDefault(toCurrency, 1.0);
//        rateCache.put(cacheKey, exchangeRate);
        redisTemplate.opsForValue().set(cacheKey, exchangeRate, CACHE_EXPIRATION, TimeUnit.SECONDS);

        return exchangeRate;
    }

    public List<TransactionResponse> getAllTransactions() {
        List<TransactionRequest> transactionRequests = transactionRepository.findAll();
        return transactionRequests.stream()
                .map(transaction -> {
                    TransactionResponse response = new TransactionResponse();
                    response.setTransactionId(transaction.getTransactionId());
                    response.setType(transaction.getType());
                    response.setAmount(transaction.getAmount());
                    response.setCurrency(transaction.getCurrency());
                    response.setDescription(transaction.getDescription());
                    response.setStatus(transaction.getStatus());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
