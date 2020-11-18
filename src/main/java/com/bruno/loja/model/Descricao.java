package com.bruno.loja.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Descricao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String comentario;

	@NotNull
	private LocalDate dataEnvio;
	
	@JsonIgnore
	@ManyToOne
	private OS ordemServico;
	
	public Descricao() {}
	

	public Descricao(Long id, @NotNull String comentario, @NotNull LocalDate dataEnvio, OS ordemServico) {
		super();
		this.id = id;
		this.comentario = comentario;
		this.dataEnvio = dataEnvio;
		this.ordemServico = ordemServico;
	}


	public OS getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OS ordemServico) {
		this.ordemServico = ordemServico;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public LocalDate getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDate dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comentario == null) ? 0 : comentario.hashCode());
		result = prime * result + ((dataEnvio == null) ? 0 : dataEnvio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ordemServico == null) ? 0 : ordemServico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Descricao other = (Descricao) obj;
		if (comentario == null) {
			if (other.comentario != null)
				return false;
		} else if (!comentario.equals(other.comentario))
			return false;
		if (dataEnvio == null) {
			if (other.dataEnvio != null)
				return false;
		} else if (!dataEnvio.equals(other.dataEnvio))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ordemServico == null) {
			if (other.ordemServico != null)
				return false;
		} else if (!ordemServico.equals(other.ordemServico))
			return false;
		return true;
	}

	
	
}
