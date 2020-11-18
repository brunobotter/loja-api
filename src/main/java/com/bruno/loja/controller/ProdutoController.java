package com.bruno.loja.controller;

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

import com.bruno.loja.model.Produto;
import com.bruno.loja.service.ProdutoService;

@RestController
@RequestMapping("api/produto")
@CrossOrigin(origins = "*")
public class ProdutoController {

	@Autowired
	private ProdutoService service;
	
	@Autowired
	private PagedResourcesAssembler<Produto> assembler;

	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscaPorId(@PathVariable Long id) {
		Produto produto = service.buscaPorId(id);
		return ResponseEntity.ok(produto);
	}
	
	@GetMapping
	public ResponseEntity<?> buscatodos(
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "limite",defaultValue = "5") int limit,
			@RequestParam(value = "order",defaultValue = "asc") String direction){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<Produto> produto =  service.buscaTodos(pageable);
			
		
		PagedModel<?> resources = assembler.toModel(produto);
	
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	
	@GetMapping("/busca_por_nome/")
	public ResponseEntity<?> buscaPorNome(
			@RequestParam(value = "nome",defaultValue = "") String nome,
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "limite",defaultValue = "5") int limit,
			@RequestParam(value = "order",defaultValue = "asc") String direction){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<Produto> produto =  service.buscaPorNome(pageable, nome);
		
		
		PagedModel<?> resources = assembler.toModel(produto);
		
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto salvar(@Valid @RequestBody Produto produto){
		Produto produtos= service.salvar(produto);
		return produtos;
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Produto> atualizar(@Valid @PathVariable Long id, @RequestBody Produto produto){
		Produto produtos = service.atualizar(produto, id);
		return ResponseEntity.ok(produtos);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Produto> deletar(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
