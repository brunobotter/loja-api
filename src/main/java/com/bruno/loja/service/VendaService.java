package com.bruno.loja.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bruno.loja.controller.exception.NegocioException;
import com.bruno.loja.controller.exception.SemElementoException;
import com.bruno.loja.controller.exception.illegalArgumentoException;
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

	public Page<Venda> buscaPorNomeCliente(Pageable pageable, String nome) {
		Page<Venda> page = vendaRepository.findVendaByNome(nome, pageable);
		return page;
	}
	

	public Page<Venda> buscaTodosPaginado(Pageable pageable) {
		Page<Venda> page = vendaRepository.findAll(pageable);
		return page;
	}


	public Venda buscaPorId(Long id) {
		return vendaRepository.findById(id)
				.orElseThrow(() -> new NegocioException("Id da venda não pode ser nulo!"));
	}

	public Venda adicionar(Venda venda) {
		Cliente cliente = clienteRepository.findById(venda.getCliente().getId())
				.orElseThrow(() -> new illegalArgumentoException("Id cliente não encontrado!"));
		Funcionario funcionario = funcionarioRepository.findById(venda.getFuncionario().getId())
				.orElseThrow(() -> new NegocioException("Id do funcionario não pode ser nulo!"));
		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
		venda.setDataVenda(LocalDate.now());
		venda.setStatusVenda(StatusVenda.ABERTA);
		return vendaRepository.save(venda);
	}

	public void deletar(Long id) {
		vendaRepository.deleteById(id);
	}

	public Venda atualizar(Venda venda, Long id) {
		Venda vendas = vendaRepository.findById(id)
				.orElseThrow(() -> new NegocioException("Id da venda não pode ser nulo!"));
		vendas.setCliente(venda.getCliente());
		vendas.setFuncionario(venda.getFuncionario());
		vendas.setId(vendas.getId());
		vendas.setDataVenda(LocalDate.now());
		return vendaRepository.save(vendas);
	}

	/* Items */

	public Item adicionarItem(Long idVenda, Long idprod, int quantidade) {
		Venda venda = vendaRepository.findById(idVenda)
				.orElseThrow(() -> new NegocioException("Id da venda não pode ser nulo!"));
		Produto produto = produtoRepository.findById(idprod)
				.orElseThrow(() -> new NegocioException("Id do produto não pode ser nulo!"));
		if (venda.getStatusVenda() == StatusVenda.ABERTA) {
			Item item = new Item();
			item.setProduto(produto);
			item.setVenda(venda);
			item.setValor(calcularTotal(quantidade, item.getProduto().getPreco()));
			item.setQuantidade(quantidade);
			return itemRepository.save(item);
		} else {
			throw new NegocioException("os fechada");
		}

	}

	public Venda calcularVenda(Long idVenda) {
		Venda venda = vendaRepository.findById(idVenda)
				.orElseThrow(() -> new NegocioException("Id da venda não pode ser nulo!"));

		BigDecimal total = BigDecimal.ZERO;
		for (Item vendas : venda.getListaItem()) {
			total = total.add(vendas.getValor());
		}
		venda.setValorTotal(total);
		return vendaRepository.save(venda);
	}

	public Venda finalizaStatusVenda(Long idVenda, BigDecimal desconto) {
		Venda venda = vendaRepository.findById(idVenda)
				.orElseThrow(() -> new NegocioException("Id da venda não pode ser nulo!"));
		if (venda.getStatusVenda().equals(StatusVenda.ABERTA)) {

			venda.setStatusVenda(StatusVenda.FINALIZADA);
			BigDecimal total = BigDecimal.ZERO;
			for (Item vendas : venda.getListaItem()) {
				total = total.add(vendas.getValor());
			}
			venda.setDesconto(desconto);
			total = total.subtract(desconto);
			venda.setValorTotal(total);
			return vendaRepository.save(venda);
		} else {
			throw new NegocioException("os ja fechada");
		}
	}

	public BigDecimal calcularTotal(int quantidade, BigDecimal valor) {
		BigDecimal total = valor.multiply(new BigDecimal(quantidade));
		return total;
	}

	public List<Item> listaPorVenda(Long idVenda) {
		Venda venda = vendaRepository.findById(idVenda)
				.orElseThrow(() -> new SemElementoException("Id da ordem de serviço não encontrado!"));
		return venda.getListaItem();
	}

	public Item buscaItemPorId(Long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new SemElementoException("Id ordem de serviço não encontrado!"));
	}

	public void deletaItemPorId(Long id) {
		itemRepository.deleteById(id);
	}

}
