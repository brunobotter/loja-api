package com.bruno.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bruno.loja.model.Descricao;

@Repository
public interface DescricaoRepository extends JpaRepository<Descricao, Long> {

}
