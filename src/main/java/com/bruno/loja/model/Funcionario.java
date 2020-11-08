package com.bruno.loja.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.bruno.loja.vo.FuncionarioVO;


@Entity
public class Funcionario extends Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String cpf;
	
	public Funcionario() {
		
	}
	
	public Funcionario(FuncionarioVO funcionarioVO) {
		this.id = funcionarioVO.getKey();
		this.cpf = funcionarioVO.getCpf();
		this.cep = funcionarioVO.getCep();
		this.cidade = funcionarioVO.getCidade();
		this.complemento = funcionarioVO.getComplemento();
		this.email = funcionarioVO.getEmail();
		this.estado = funcionarioVO.getEstado();
		this.logradouro = funcionarioVO.getLogradouro();
		this.nome = funcionarioVO.getNome();
		this.numero = funcionarioVO.getNumero();
		this.telefone = funcionarioVO.getTelefone();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
