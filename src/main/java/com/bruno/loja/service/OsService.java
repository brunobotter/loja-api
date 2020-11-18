package com.bruno.loja.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.SemElementoException;
import com.bruno.loja.controller.exception.illegalArgumentoException;
import com.bruno.loja.model.Cliente;
import com.bruno.loja.model.Descricao;
import com.bruno.loja.model.Funcionario;
import com.bruno.loja.model.OS;
import com.bruno.loja.model.StatusOs;
import com.bruno.loja.repository.ClienteRepository;
import com.bruno.loja.repository.DescricaoRepository;
import com.bruno.loja.repository.FuncionarioRepository;
import com.bruno.loja.repository.OsRepository;



@Service
public class OsService {

	@Autowired
	private OsRepository osRepository;
	
	@Autowired
	private DescricaoRepository descRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	
	public OS buscarPorId(Long id) {
		return osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id ordem de serviço não encontrado!"));
	}
	
	public List<OS> consultaOsAberta(OS os) {
		return osRepository.findByOsAberta(os);
		
	}
	
	public Page<OS> buscaPorNomeCliente(Pageable pageable, String nome){
		Page<OS> page = osRepository.findOsByNome(nome, pageable);
		return page;
	}
	
	public Page<OS> buscaTodosPaginado(Pageable pageable){
		Page<OS> page = osRepository.findAll(pageable);
		return page;
	}
	
	public List<OS> buscaTodos(){
		return osRepository.findAll();
	
	}
	
	
	public void deletar(Long id) {
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id ordem de serviço não encontrado!"));
		osRepository.delete(os);
	}
	
	public OS salvar(OS os) {
		Cliente cliente = clienteRepository.findById(os.getCliente().getId()).orElseThrow(()
				-> new illegalArgumentoException("Id cliente não encontrado!"));
		Funcionario funcionario = funcionarioRepository.findById(os.getFuncionario().getId()).orElseThrow(()
				-> new illegalArgumentoException("Id Funcionario não encontrado!"));
		os.setCliente(cliente);
		os.setFuncionario(funcionario);
		os.setStatus(StatusOs.ABERTA);
		os.setDataEntrada(LocalDate.now());
		
		return osRepository.save(os);
		
	}
	
	
	public OS atualizar(OS os, Long id) {
		OS os1 = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		os1.setId(id);
		os1.setCliente(os.getCliente());
		os1.setFuncionario(os.getFuncionario());
		os1.setDataEntrada(os.getDataEntrada());
		os1.setDataSaida(os.getDataSaida());
		os1.setPreco(os.getPreco());
		os1.setStatus(os.getStatus());
		return osRepository.save(os1);
	}
	
	public OS finalizarOs(Long id) {
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		os.setStatus(StatusOs.FINALIZADA);
		os.setDataSaida(LocalDate.now());
		return osRepository.save(os);
	}
	
	/*Descrição*/
	
	public Descricao adicionarComentario(Descricao comentario, Long id) {
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		Descricao descricao = new Descricao();
		descricao.setOrdemServico(os);
		descricao.setComentario(comentario.getComentario());
		descricao.setDataEnvio(LocalDate.now());
		return descRepository.save(descricao);
	}
	
	public List<Descricao> buscaComentarios(){
		return descRepository.findAll();
		
	}
	
	
	public List<Descricao> buscaComentariosPorOs(Long id){
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		List<Descricao> listaDescricao = os.getComentarios();
		return listaDescricao;
	}
	
	public void deletarComentario(Long id) {
		descRepository.deleteById(id);
	}
	

}
