package com.bruno.loja.controller;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

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
import com.bruno.loja.vo.ItemVO;
import com.bruno.loja.vo.VendaVO;

@RestController
@RequestMapping("api/venda")
@CrossOrigin(origins = "*")
public class VendaController {

	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private PagedResourcesAssembler<VendaVO> assembler;
	
	
	
	@SuppressWarnings("deprecation")
	@GetMapping("/{id}")
	public VendaVO buscaPorId(@PathVariable Long id) {
		VendaVO venda =  vendaService.buscaPorId(id);
		venda.add(linkTo(methodOn(VendaController.class).buscaPorId(id)).withSelfRel());
		return venda;
	}
	

	
	@SuppressWarnings("deprecation")
	@GetMapping
	public ResponseEntity<?> buscarTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<VendaVO> vendaVo = vendaService.buscaTodosPaginado(pageable);
		vendaVo.stream()
				.forEach(p -> p.add(linkTo(methodOn(VendaController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(vendaVo);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@GetMapping("/calcula/{idVenda}")
	public VendaVO calculaVendas(@PathVariable Long idVenda) {
		return vendaService.calcularVenda(idVenda);
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public VendaVO adicionar(@Valid @RequestBody Venda venda) {
		VendaVO vendaVo =  vendaService.adicionar(venda); 
		vendaVo.add(linkTo(methodOn(VendaController.class).buscaPorId(venda.getId())).withSelfRel());
		return vendaVo;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<VendaVO> deletar(@PathVariable Long id) {
		 vendaService.deletar(id);
		 return ResponseEntity.noContent().build();
	}
	
	@SuppressWarnings("deprecation")
	@PutMapping("/{id}")
	public VendaVO atualizar(@Valid @PathVariable Long id, @RequestBody Venda venda){
		VendaVO vendaVo =  vendaService.atualizar(venda, id); 
		vendaVo.add(linkTo(methodOn(VendaController.class).buscaPorId(venda.getId())).withSelfRel());
		return vendaVo;
	}
	
	@PutMapping("/{idVenda}/{desconto}/finalizada")
	public void finalizaVenda(@Valid @PathVariable Long idVenda, @PathVariable BigDecimal desconto){
		vendaService.finalizaStatusVenda(idVenda, desconto);
	}
	
	/*Item*/
	
	@SuppressWarnings("deprecation")
	@PutMapping("/adicionar/{idVenda}/{idProd}")
	public ItemVO adicionarItem(@Valid @PathVariable Long idVenda, @PathVariable Long idProd,
			@RequestBody Item item) {
		ItemVO novoItem = vendaService.adicionarItem(idVenda, idProd, item.getQuantidade());
		novoItem.add(linkTo(methodOn(VendaController.class).buscaPorId(item.getId())).withSelfRel());
		return novoItem;
	}
	
	@GetMapping("/item/{idVenda}")
	public List<ItemVO> listaItensPorVenda(@PathVariable Long idVenda){
		return vendaService.listaPorVenda(idVenda);	
	}
	
	@GetMapping("/item/busca/{idItem}")
	public ItemVO buscaItemPorId(@PathVariable Long idItem) {
		return vendaService.buscaItemPorId(idItem);
	}

	
	@DeleteMapping("/item/{idItem}")
	public ResponseEntity<Item> deletaPorIdItem(@PathVariable Long idItem){
		vendaService.deletaItemPorId(idItem);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
