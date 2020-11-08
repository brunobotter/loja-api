package com.bruno.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.converter.DozerConverter;
import com.bruno.loja.model.Produto;
import com.bruno.loja.repository.ProdutoRepository;
import com.bruno.loja.vo.ProdutoVO;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;

	public ProdutoVO buscaPorId(Long id) {
		var entidade = repository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		return DozerConverter.parseObject(entidade, ProdutoVO.class);
	}

	public Page<ProdutoVO> buscaTodos(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToProdutoVo);
	}
	
	public Page<ProdutoVO> buscaPorNome(Pageable pageable, String nome) {
		var page = repository.findProdutoByNome(nome, pageable);
		return page.map(this::convertToProdutoVo);
	}

	public ProdutoVO salvar(ProdutoVO produto) {
		var entidade = DozerConverter.parseObject(produto, Produto.class);
		var produtoVo = DozerConverter.parseObject(repository.save(entidade), ProdutoVO.class);
		return produtoVo;
	}

	public ProdutoVO atualizar(ProdutoVO produto, Long id) {
		var entidade =  repository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		entidade.setNome(produto.getNome());
		entidade.setPreco(produto.getPreco());
		var produtoVo = DozerConverter.parseObject(repository.save(entidade), ProdutoVO.class);
			return produtoVo;

	}

	public void deletar(Long id) {
		var entidade =  repository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		repository.delete(entidade);
	}
	
	
	private ProdutoVO convertToProdutoVo(Produto entidade) {
		return DozerConverter.parseObject(entidade, ProdutoVO.class);
	}
	
}
