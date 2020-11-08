package com.bruno.loja.vo;

import com.bruno.loja.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@JsonPropertyOrder({ "id", "cpf", "cnpj" })
public class ClienteVO extends PessoaVO{

	private static final long serialVersionUID = 1L;

	@Mapping("id")
	@JsonProperty("id")
	protected Long key;
	
	private String cpf;
	
	private String cnpj;
	
	
	public ClienteVO() {
		
	}
	
	public ClienteVO(Cliente cliente) {
		this.key = cliente.getId();
		this.cpf = cliente.getCpf();
		this.cnpj = cliente.getCnpj();
		this.nome = cliente.getNome();
		this.telefone = cliente.getTelefone();
		this.email = cliente.getEmail();
		this.logradouro = cliente.getLogradouro();
		this.cidade = cliente.getCidade();
		this.estado = cliente.getEstado();
		this.numero = cliente.getNumero();
		this.complemento = cliente.getComplemento();
		this.cep = cliente.getCep();
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
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
		ClienteVO other = (ClienteVO) obj;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
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
