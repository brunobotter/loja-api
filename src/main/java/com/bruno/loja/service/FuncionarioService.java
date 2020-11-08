package com.bruno.loja.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.converter.DozerConverter;
import com.bruno.loja.model.Funcionario;
import com.bruno.loja.repository.FuncionarioRepository;
import com.bruno.loja.vo.FuncionarioVO;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository repository;
	
	public FuncionarioVO buscaPorId(Long id) {
		var entidade =  repository.findById(id).orElseThrow(()
				-> new NegocioException("Id funcionario não encontrado!"));
		return DozerConverter.parseObject(entidade, FuncionarioVO.class);
	}
	
	public Page<FuncionarioVO> buscaTodosPaginado(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToProdutoVo);
	}
	
	public List<FuncionarioVO> buscaTodos() {
		List<Funcionario> listaFuncionario = repository.findAll();
		List<FuncionarioVO> listaFuncionarioVO = listaFuncionario.stream().map(c -> new FuncionarioVO(c))
				.collect(Collectors.toList());
		return listaFuncionarioVO;
	}
	
	public Page<FuncionarioVO> buscaPorNome(Pageable pageable, String nome) {
		var page = repository.findFuncionarioByNome(nome, pageable);
		return page.map(this::convertToProdutoVo);
	}
	
	public FuncionarioVO salva(FuncionarioVO func) {
		var entidade = DozerConverter.parseObject(func, Funcionario.class);
		var funcionarioVO = DozerConverter.parseObject(repository.save(entidade), FuncionarioVO.class);
		return funcionarioVO;
	}
	
	public void deletaPorId(Long id) {
		var entidade = repository.findById(id).orElseThrow(()->
		new NegocioException("Id Funcionario não encontrado!"));
		repository.delete(entidade);
	}
	
	public FuncionarioVO atualiza(FuncionarioVO func, Long id) {
		var entidade = repository.findById(id).orElseThrow(()
				-> new NegocioException("Id cliente não encontrado!"));;
			entidade.setNome(func.getNome());
			entidade.setCep(func.getCep());
			entidade.setCidade(func.getCidade());
			entidade.setComplemento(func.getComplemento());
			entidade.setCpf(func.getCpf());
			entidade.setEmail(func.getEmail());
			entidade.setEstado(func.getEstado());
			entidade.setLogradouro(func.getLogradouro());
			entidade.setNumero(func.getNumero());
			entidade.setTelefone(func.getTelefone());
			var funcionarioVO = DozerConverter.parseObject(repository.save(entidade), FuncionarioVO.class);
			return funcionarioVO;
	
	}
	
	private FuncionarioVO convertToProdutoVo(Funcionario entidade) {
		return DozerConverter.parseObject(entidade, FuncionarioVO.class);
	}
}
