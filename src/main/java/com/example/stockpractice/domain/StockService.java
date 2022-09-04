package com.example.stockpractice.domain;


import org.hibernate.engine.transaction.internal.SynchronizationRegistryImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }


    //@Transactional
    public synchronized void decrease(Long id, Long quantity){

        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        this.stockRepository.saveAndFlush(stock);
    }
}
