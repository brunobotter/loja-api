package com.bruno.loja.controller.exception;

import java.util.NoSuchElementException;

public class SemElementoException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public SemElementoException(String message) {
		super(message);
	}
}
