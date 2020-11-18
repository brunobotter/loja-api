package com.bruno.loja.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.loja.model.Item;
import com.bruno.loja.model.Venda;
import com.bruno.loja.service.VendaService;

@RestController
@RequestMapping("api/venda")
@CrossOrigin(origins = "*")
public class VendaController {

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private PagedResourcesAssembler<Venda> assembler;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Venda> buscaPorId(@PathVariable Long id) {
		Venda venda =  vendaService.buscaPorId(id);
		return ResponseEntity.ok(venda);
	}
	

	
	@GetMapping
	public ResponseEntity<?> buscarTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<Venda> venda = vendaService.buscaTodosPaginado(pageable);
		

		PagedModel<?> resources = assembler.toModel(venda);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@GetMapping("/busca_por_nome/")
	public ResponseEntity<?> buscaTodosPorNomeCliente(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<Venda> venda = vendaService.buscaPorNomeCliente(pageable, nome);
		

		PagedModel<?> resources = assembler.toModel(venda);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@GetMapping("/calcula/{idVenda}")
	public ResponseEntity<Venda> calculaVendas(@PathVariable Long idVenda) {
		Venda venda = vendaService.calcularVenda(idVenda);
		return ResponseEntity.ok(venda);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Venda adicionar(@Valid @RequestBody Venda venda) {
		return vendaService.adicionar(venda); 
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Venda> deletar(@PathVariable Long id) {
		 vendaService.deletar(id);
		 return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Venda> atualizar(@Valid @PathVariable Long id, @RequestBody Venda venda){
		Venda vendas =  vendaService.atualizar(venda, id); 
		return ResponseEntity.ok(vendas);
	}
	
	@PutMapping("/{idVenda}/{desconto}/finalizada")
	public void finalizaVenda(@Valid @PathVariable Long idVenda, @PathVariable BigDecimal desconto){
		vendaService.finalizaStatusVenda(idVenda, desconto);
	}
	
	/*Item*/
	
	@PutMapping("/adicionar/{idVenda}/{idProd}")
	public ResponseEntity<Item> adicionarItem(@Valid @PathVariable Long idVenda, @PathVariable Long idProd,
			@RequestBody Item item) {
		Item novoItem = vendaService.adicionarItem(idVenda, idProd, item.getQuantidade());
		return ResponseEntity.ok(novoItem);
	}
	
	@GetMapping("/item/{idVenda}")
	public ResponseEntity<List<Item>> listaItensPorVenda(@PathVariable Long idVenda){
		List<Item> item = vendaService.listaPorVenda(idVenda);
		return ResponseEntity.ok(item);
	}
	
	@GetMapping("/item/busca/{idItem}")
	public ResponseEntity<Item> buscaItemPorId(@PathVariable Long idItem) {
		Item item = vendaService.buscaItemPorId(idItem);
		return ResponseEntity.ok(item);
	}

	
	@DeleteMapping("/item/{idItem}")
	public ResponseEntity<Item> deletaPorIdItem(@PathVariable Long idItem){
		vendaService.deletaItemPorId(idItem);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
