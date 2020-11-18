package com.bruno.loja.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.model.Cliente;
import com.bruno.loja.repository.ClienteRepository;


@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente buscaPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));	
	}
	
	public List<Cliente> buscaTodos(){
		return clienteRepository.findAll();
	}

	public Page<Cliente> buscaTodosPaginado(Pageable pageable) {
		Page<Cliente> page = clienteRepository.findAll(pageable);
		return page;
	}
	

	
	public Page<Cliente> buscaPorNome(Pageable pageable, String nome) {
		Page<Cliente> page = clienteRepository.findClienteByNome(nome, pageable);
		return page;
	}
	
	public Cliente salvar(Cliente produto) {
		return clienteRepository.save(produto);		
	}

	public void deletar(Long id) {
		Cliente cliente =  clienteRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id do produto não encontrado!"));
		clienteRepository.delete(cliente);
	}
	
	public Cliente atualiza(Cliente cli, Long id) {
		Cliente cliente = clienteRepository.findById(id).orElseThrow(()
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
				return clienteRepository.save(cliente);		 
	}


}
