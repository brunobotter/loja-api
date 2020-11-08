package com.bruno.loja.controller.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;

public class illegalArgumentoException extends InvalidDataAccessApiUsageException {

	private static final long serialVersionUID = 1L;

	public illegalArgumentoException(String mensagem) {
		super(mensagem);
	}
}
