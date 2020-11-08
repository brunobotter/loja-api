package com.bruno.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bruno.loja.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

}
