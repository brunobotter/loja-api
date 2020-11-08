package com.bruno.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bruno.loja.model.Produto;



@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query("SELECT p FROM Produto p WHERE p.nome LIKE LOWER(CONCAT ('%', :nome,'%')) ")
	Page<Produto> findProdutoByNome(@Param("nome") String nome, Pageable pageable);
}
