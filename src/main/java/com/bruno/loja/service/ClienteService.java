package com.bruno.loja.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.converter.DozerConverter;
import com.bruno.loja.model.Cliente;
import com.bruno.loja.repository.ClienteRepository;
import com.bruno.loja.vo.ClienteVO;


@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	public ClienteVO buscaPorId(Long id) {
		var entidade = repository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		return DozerConverter.parseObject(entidade, ClienteVO.class);
	}

	public Page<ClienteVO> buscaTodosPaginado(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToProdutoVo);
	}
	
	public List<ClienteVO> buscaTodos() {
		List<Cliente> listaCliente = repository.findAll();
		List<ClienteVO> listaClienteVO = listaCliente.stream().map(c -> new ClienteVO(c))
				.collect(Collectors.toList());
		return listaClienteVO;
	}
	
	public Page<ClienteVO> buscaPorNome(Pageable pageable, String nome) {
		var page = repository.findClienteByNome(nome, pageable);
		return page.map(this::convertToProdutoVo);
	}

	public ClienteVO salvar(ClienteVO produto) {
		var entidade = DozerConverter.parseObject(produto, Cliente.class);
		var clienteVo = DozerConverter.parseObject(repository.save(entidade), ClienteVO.class);
		return clienteVo;
	}

	public void deletar(Long id) {
		var entidade =  repository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		repository.delete(entidade);
	}
	
	public ClienteVO atualiza(ClienteVO cli, Long id) {
		var cliente = repository.findById(id).orElseThrow(()
				-> new NegocioException("Id cliente não encontrado!"));;
				cliente.setNome(cli.getNome());
				cliente.setCep(cli.getCep());
				cliente.setCidade(cli.getCidade());
				cliente.setComplemento(cli.getComplemento());
				cliente.setCpf(cli.getCpf());
				cliente.setEmail(cli.getEmail());
				cliente.setEstado(cli.getEstado());
				cliente.setLogradouro(cli.getLogradouro());
				cliente.setNumero(cli.getNumero());
				cliente.setTelefone(cli.getTelefone());
				var clienteVo = DozerConverter.parseObject(repository.save(cliente), ClienteVO.class);
				return clienteVo;
	}
	
	private ClienteVO convertToProdutoVo(Cliente entidade) {
		return DozerConverter.parseObject(entidade, ClienteVO.class);
	}
	

}
