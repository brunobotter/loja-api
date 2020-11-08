package com.bruno.loja.controller.exception;

public class RelatorioException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RelatorioException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}
	
	public RelatorioException(String mensagem) {
		super(mensagem);
	}
	
}
