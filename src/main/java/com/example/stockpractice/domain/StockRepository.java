package com.example.stockpractice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository  extends JpaRepository<Stock,Long> {

}
