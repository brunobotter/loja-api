package com.bruno.loja.vo;

import com.bruno.loja.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@JsonPropertyOrder({ "id", "cpf" })
public class FuncionarioVO extends PessoaVO {

	private static final long serialVersionUID = 1L;

	@Mapping("id")
	@JsonProperty("id")
	protected Long key;
	private String cpf;

	public FuncionarioVO() {
		
	}
	
	
	public FuncionarioVO(Funcionario funcionario) {
		this.key = funcionario.getId();
		this.cpf = funcionario.getCpf();
		this.nome = funcionario.getNome();
		this.telefone = funcionario.getTelefone();
		this.email = funcionario.getEmail();
		this.logradouro = funcionario.getLogradouro();
		this.cidade = funcionario.getCidade();
		this.estado = funcionario.getEstado();
		this.numero = funcionario.getNumero();
		this.complemento = funcionario.getComplemento();
		this.cep = funcionario.getCep();
	}
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		FuncionarioVO other = (FuncionarioVO) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
