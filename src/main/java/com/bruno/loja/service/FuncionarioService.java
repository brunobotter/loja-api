package com.bruno.loja.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.model.Funcionario;
import com.bruno.loja.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	public Funcionario buscaPorId(Long id) {
		return funcionarioRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id funcionario não encontrado!"));
	}
	
	public Page<Funcionario> buscaTodosPaginado(Pageable pageable) {
		Page<Funcionario> page = funcionarioRepository.findAll(pageable);
		return page;
	}
	
	public List<Funcionario> buscaTodos() {
		return funcionarioRepository.findAll();
	}
	
	public Page<Funcionario> buscaPorNome(Pageable pageable, String nome) {
		Page<Funcionario> page = funcionarioRepository.findFuncionarioByNome(nome, pageable);
		return page;
	}
	
	public Funcionario salva(Funcionario func) {
		return funcionarioRepository.save(func);
	}
	
	public void deletaPorId(Long id) {
		Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(()->
		new NegocioException("Id Funcionario não encontrado!"));
		funcionarioRepository.delete(funcionario);
	}
	
	public Funcionario atualiza(Funcionario func, Long id) {
		Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id cliente não encontrado!"));;
				funcionario.setNome(func.getNome());
				funcionario.setCep(func.getCep());
				funcionario.setCidade(func.getCidade());
				funcionario.setComplemento(func.getComplemento());
				funcionario.setCpf(func.getCpf());
				funcionario.setEmail(func.getEmail());
				funcionario.setEstado(func.getEstado());
				funcionario.setLogradouro(func.getLogradouro());
				funcionario.setNumero(func.getNumero());
				funcionario.setTelefone(func.getTelefone());
				return funcionarioRepository.save(funcionario);
	}
	

}
