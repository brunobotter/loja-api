package com.bruno.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bruno.loja.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
