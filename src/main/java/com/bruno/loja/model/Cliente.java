package com.bruno.loja.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.bruno.loja.vo.ClienteVO;

@Entity
public class Cliente extends Pessoa {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cpf;
	
	private String cnpj;
	
	public Cliente() {
		
	}
	
	public Cliente(ClienteVO clienteVO) {
		this.id = clienteVO.getKey();
		this.cpf = clienteVO.getCpf();
		this.cnpj = clienteVO.getCnpj();
		this.cep = clienteVO.getCep();
		this.cidade = clienteVO.getCidade();
		this.complemento = clienteVO.getComplemento();
		this.email = clienteVO.getEmail();
		this.estado = clienteVO.getEstado();
		this.logradouro = clienteVO.getLogradouro();
		this.nome = clienteVO.getNome();
		this.numero = clienteVO.getNumero();
		this.telefone = clienteVO.getTelefone();
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
		Cliente other = (Cliente) obj;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
