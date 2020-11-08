package com.bruno.loja.controller.exception;

public class SQLIntegrityConstraintViolationException extends java.sql.SQLIntegrityConstraintViolationException {

	private static final long serialVersionUID = 1L;

	public SQLIntegrityConstraintViolationException(String mensagem) {
		super(mensagem);
	}
}
