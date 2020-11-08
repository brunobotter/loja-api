package com.bruno.loja.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.controller.exception.SemElementoException;
import com.bruno.loja.controller.exception.illegalArgumentoException;
import com.bruno.loja.converter.DozerConverter;
import com.bruno.loja.model.Cliente;
import com.bruno.loja.model.Funcionario;
import com.bruno.loja.model.Item;
import com.bruno.loja.model.Produto;
import com.bruno.loja.model.StatusVenda;
import com.bruno.loja.model.Venda;
import com.bruno.loja.repository.ClienteRepository;
import com.bruno.loja.repository.FuncionarioRepository;
import com.bruno.loja.repository.ItemRepository;
import com.bruno.loja.repository.ProdutoRepository;
import com.bruno.loja.repository.VendaRepository;
import com.bruno.loja.vo.ItemVO;
import com.bruno.loja.vo.VendaVO;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired 
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired 
	private ItemRepository itemRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	

	
	public Page<VendaVO> buscaTodosPaginado(Pageable pageable){
		var page = vendaRepository.findAll(pageable);
		return page.map(this::convertToVendaVo);
	}
	
	private VendaVO convertToVendaVo(Venda entidade) {
		return DozerConverter.parseObject(entidade, VendaVO.class);
	}
	


	
	public VendaVO buscaPorId(Long id) {
		var entidade =  vendaRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id da venda não pode ser nulo!"));
		return DozerConverter.parseObject(entidade, VendaVO.class);
	}
	
	public VendaVO adicionar(Venda venda) {
		Cliente cliente = clienteRepository.findById(venda.getCliente().getId()).orElseThrow(()
				-> new illegalArgumentoException("Id cliente não encontrado!"));
		Funcionario funcionario = funcionarioRepository.findById(venda.getFuncionario().getId()).orElseThrow(()
				-> new NegocioException("Id do funcionario não pode ser nulo!"));
		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
		venda.setDataVenda(LocalDate.now());
		venda.setStatusVenda(StatusVenda.ABERTA);
		var entidade = DozerConverter.parseObject(venda, Venda.class);
		var convertido = DozerConverter.parseObject(vendaRepository.save(entidade), VendaVO.class);
		return convertido;
	}
	
	public void deletar(Long id) {
		vendaRepository.deleteById(id);
	}
	
	public VendaVO atualizar(Venda venda, Long id) {
		Venda vendas = vendaRepository.findById(id).orElseThrow(()
				-> new NegocioException("Id da venda não pode ser nulo!"));
		if(vendas.getCliente() == null) {
			venda.setCliente(venda.getCliente());
		}
		if(vendas.getFuncionario() == null) {
			venda.setFuncionario(venda.getFuncionario());
		}
		venda.setDataVenda(LocalDate.now());
		var entidade = DozerConverter.parseObject(venda, Venda.class);
		var convertido = DozerConverter.parseObject(vendaRepository.save(entidade), VendaVO.class);
		return convertido;
	}
	
	
	
	/*Items*/
	
	public ItemVO adicionarItem(Long idVenda, Long idprod, int quantidade) {
		Venda venda = vendaRepository.findById(idVenda).orElseThrow(()
				-> new NegocioException("Id da venda não pode ser nulo!"));
		Produto produto = produtoRepository.findById(idprod).orElseThrow(()
				-> new NegocioException("Id do produto não pode ser nulo!"));
		if(venda.getStatusVenda() == StatusVenda.ABERTA) {
		Item item = new Item();
		item.setProduto(produto);
		item.setVenda(venda);
		item.setValor(calcularTotal(quantidade, item.getProduto().getPreco()));
		item.setQuantidade(quantidade);
		var entidade = DozerConverter.parseObject(item, Item.class);
		var convertido = DozerConverter.parseObject(itemRepository.save(entidade), ItemVO.class);
		return convertido;
		}else {
			throw new NegocioException("os fechada");
		}
		
	}
	
	public VendaVO calcularVenda(Long idVenda) {
		Venda venda = vendaRepository.findById(idVenda).orElseThrow(()
				-> new NegocioException("Id da venda não pode ser nulo!"));

		BigDecimal total = BigDecimal.ZERO;
		for (Item vendas : venda.getListaItem()) {
			total = total.add(vendas.getValor());
		}
		venda.setValorTotal(total);
		var entidade = DozerConverter.parseObject(venda, Venda.class);
		var convertido = DozerConverter.parseObject(vendaRepository.save(entidade), VendaVO.class);
		return convertido;
	}
	
	
	public VendaVO finalizaStatusVenda(Long idVenda, BigDecimal desconto) {
		Venda venda = vendaRepository.findById(idVenda).orElseThrow(()
				-> new NegocioException("Id da venda não pode ser nulo!"));
		if(venda.getStatusVenda().equals(StatusVenda.ABERTA)) {

		venda.setStatusVenda(StatusVenda.FINALIZADA);
		BigDecimal total = BigDecimal.ZERO;
		for (Item vendas : venda.getListaItem()) {
			total = total.add(vendas.getValor());
		}
		venda.setDesconto(desconto);
		total = total.subtract(desconto);
		venda.setValorTotal(total);
		var entidade = DozerConverter.parseObject(venda, Venda.class);
		var convertido = DozerConverter.parseObject(vendaRepository.save(entidade), VendaVO.class);
		return convertido;
		}else {
			throw new NegocioException("os ja fechada");
		}
	}
	
	public BigDecimal calcularTotal(int quantidade, BigDecimal valor) {
		BigDecimal total = valor.multiply(new BigDecimal(quantidade));
		return total;
	}
	
	
	public List<ItemVO> listaPorVenda(Long idVenda){
		Venda venda = vendaRepository.findById(idVenda).orElseThrow(()
				-> new SemElementoException("Id da ordem de serviço não encontrado!"));
		List<Item> listaItem = venda.getListaItem();
		List<ItemVO> listaItemvo = listaItem.stream().map(c -> new ItemVO(c))
				.collect(Collectors.toList());
		return listaItemvo;
	}
	
	
	public ItemVO buscaItemPorId(Long id) {
		var entidade = itemRepository.findById(id).orElseThrow(()
				-> new SemElementoException("Id ordem de serviço não encontrado!"));
		return DozerConverter.parseObject(entidade, ItemVO.class);
	}
	
	public void deletaItemPorId(Long id) {
		itemRepository.deleteById(id);
	}
	


	
	
}
