package com.bruno.loja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bruno.loja.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	@Query("SELECT p FROM Funcionario p WHERE p.nome LIKE LOWER(CONCAT ('%', :nome,'%')) ")
	Page<Funcionario> findFuncionarioByNome(@Param("nome") String nome, Pageable pageable);
}
