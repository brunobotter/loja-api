package com.bruno.loja.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.SemElementoException;
import com.bruno.loja.controller.exception.illegalArgumentoException;
import com.bruno.loja.converter.DozerConverter;
import com.bruno.loja.model.Cliente;
import com.bruno.loja.model.Descricao;
import com.bruno.loja.model.Funcionario;
import com.bruno.loja.model.OS;
import com.bruno.loja.model.StatusOs;
import com.bruno.loja.repository.ClienteRepository;
import com.bruno.loja.repository.DescricaoRepository;
import com.bruno.loja.repository.FuncionarioRepository;
import com.bruno.loja.repository.OsRepository;
import com.bruno.loja.vo.DescricaoVO;
import com.bruno.loja.vo.OsVO;



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
	
	
	
	public OsVO buscarPorId(Long id) {
		var entidade = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id ordem de serviço não encontrado!"));
		return DozerConverter.parseObject(entidade, OsVO.class);
	}
	
	public Page<OsVO> buscaPorNomeCliente(Pageable pageable, String nome){
		var page = osRepository.findOsByNome(nome, pageable);
		return page.map(this::convertToOsVo);
	}
	
	public Page<OsVO> buscaTodosPaginado(Pageable pageable){
		var page = osRepository.findAll(pageable);
		return page.map(this::convertToOsVo);
	}
	
	public List<OsVO> buscaTodos(){
		List<OS> listaOs = osRepository.findAll();
		List<OsVO> listaOSVO = listaOs.stream().map(c -> new OsVO(c))
				.collect(Collectors.toList());
		return listaOSVO;
	}
	
	public void deletar(Long id) {
		var entidade = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id ordem de serviço não encontrado!"));
		osRepository.delete(entidade);
	}
	
	public OsVO salvar(OS os) {
		Cliente cliente = clienteRepository.findById(os.getCliente().getId()).orElseThrow(()
				-> new illegalArgumentoException("Id cliente não encontrado!"));
		Funcionario funcionario = funcionarioRepository.findById(os.getFuncionario().getId()).orElseThrow(()
				-> new illegalArgumentoException("Id Funcionario não encontrado!"));
		os.setCliente(cliente);
		os.setFuncionario(funcionario);
		os.setStatus(StatusOs.ABERTA);
		os.setDataEntrada(LocalDate.now());
		var entidade = DozerConverter.parseObject(os, OS.class);
		var convertido = DozerConverter.parseObject(osRepository.save(entidade), OsVO.class);
		return convertido;
	}
	
	
	public OsVO atualizar(OS os) {
		OS os1 = osRepository.findById(os.getId()).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		Cliente cliente = clienteRepository.findById(os.getCliente().getId()).orElseThrow(()
				-> new SemElementoException("Id do cliente não encontrado!"));
		Funcionario funcionario = funcionarioRepository.findById(os1.getFuncionario().getId()).orElseThrow(()
				-> new SemElementoException("Id do funcionario não encontrado!"));
		os.setId(os1.getId());
		os.setCliente(cliente);
		os.setFuncionario(funcionario);
		os.setDataEntrada(os1.getDataEntrada());
		os.setDataSaida(os1.getDataSaida());
		os.setPreco(os1.getPreco());
		os.setStatus(os1.getStatus());
		var entidade = DozerConverter.parseObject(os, OS.class);
		var convertido = DozerConverter.parseObject(osRepository.save(entidade), OsVO.class);
		return convertido;
	}
	
	public OsVO finalizarOs(Long id) {
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		os.setStatus(StatusOs.FINALIZADA);
		os.setDataSaida(LocalDate.now());
		var entidade = DozerConverter.parseObject(os, OS.class);
		var convertido = DozerConverter.parseObject(osRepository.save(entidade), OsVO.class);
		return convertido;
	}
	
	/*Descrição*/
	
	public DescricaoVO adicionarComentario(DescricaoVO comentario, Long id) {
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		Descricao descricao = new Descricao();
		descricao.setOrdemServico(os);
		descricao.setComentario(comentario.getComentario());
		descricao.setDataEnvio(LocalDate.now());
		var entidade = DozerConverter.parseObject(descricao, Descricao.class);
		var convertido = DozerConverter.parseObject(descRepository.save(entidade), DescricaoVO.class);
		return convertido;
	}
	
	public List<DescricaoVO> buscaComentarios(){
		List<Descricao> listaDescricao = descRepository.findAll();
		List<DescricaoVO> listaDescricaoVO = listaDescricao.stream().map(c -> new DescricaoVO(c))
				.collect(Collectors.toList());
		return listaDescricaoVO;
	}
	
	
	public List<DescricaoVO> buscaComentariosPorOs(Long id){
		OS os = osRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		List<Descricao> listaDescricao = os.getComentarios();
		List<DescricaoVO> listaDescricaoVO = listaDescricao.stream().map(c -> new DescricaoVO(c))
				.collect(Collectors.toList());
		return listaDescricaoVO;
	}
	
	public void deletarComentario(Long id) {
		descRepository.deleteById(id);
	}
	
	
	private OsVO convertToOsVo(OS entidade) {
		return DozerConverter.parseObject(entidade, OsVO.class);
	}
}
