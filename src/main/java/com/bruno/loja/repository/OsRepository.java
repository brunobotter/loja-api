package com.bruno.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bruno.loja.model.OS;

@Repository
public interface OsRepository extends JpaRepository<OS, Long>{

	@Query("SELECT p FROM OS p WHERE p.cliente.nome LIKE LOWER(CONCAT ('%', :nome,'%')) ")
	Page<OS> findOsByNome(@Param("nome") String nome, Pageable pageable);
}
