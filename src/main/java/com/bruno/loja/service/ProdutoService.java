package com.bruno.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.model.Produto;
import com.bruno.loja.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public Produto buscaPorId(Long id) {
		return produtoRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
	}

	public Page<Produto> buscaTodos(Pageable pageable) {
		Page<Produto> page = produtoRepository.findAll(pageable);
		return page;
	}
	
	public Page<Produto> buscaPorNome(Pageable pageable, String nome) {
		Page<Produto> page= produtoRepository.findProdutoByNome(nome, pageable);
		return page;
	}
	


	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}

	public Produto atualizar(Produto produto, Long id) {
		Produto produtos =  produtoRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		produtos.setNome(produto.getNome());
		produtos.setPreco(produto.getPreco());
		return produtoRepository.save(produtos);
	}

	public void deletar(Long id) {
		Produto produto =  produtoRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		produtoRepository.delete(produto);
	}
	

}
