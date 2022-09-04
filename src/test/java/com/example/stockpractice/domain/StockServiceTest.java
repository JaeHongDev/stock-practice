package com.example.stockpractice.domain;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void init(){
        this.stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    public void removeAll(){
        this.stockRepository.deleteAll();
    }

    @Test
    public void 감소테스트(){

        this.stockService.decrease(1L, 1L);
        var stock = this.stockRepository.findById(1L).orElseThrow();
        Assertions.assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    public void 동시에_100개의_요청보내기(){
        int threadCount = 100;
        var executorService = Executors.newFixedThreadPool(32);
        var latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++){
            executorService.submit(()->{
                try{
                    stockService.decrease(1L, 1L);
                }finally {
                    latch.countDown();
                }
            });
        }

        var stock = stockRepository.findById(1L).orElseThrow();
        Assertions.assertThat(stock.getQuantity()).isEqualTo(0);
    }
}