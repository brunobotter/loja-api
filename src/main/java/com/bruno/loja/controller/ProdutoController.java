package com.bruno.loja.controller;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

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

import com.bruno.loja.service.ProdutoService;
import com.bruno.loja.vo.ProdutoVO;

@RestController
@RequestMapping("api/produto")
@CrossOrigin(origins = "*")
public class ProdutoController {

	@Autowired
	private ProdutoService service;
	
	@Autowired
	private PagedResourcesAssembler<ProdutoVO> assembler;
	
	@SuppressWarnings("deprecation")
	@GetMapping("/{id}")
	public ProdutoVO buscaPorId(@PathVariable Long id) {
		ProdutoVO produtoVo = service.buscaPorId(id);
		produtoVo.add(linkTo(methodOn(ProdutoController.class).buscaPorId(id)).withSelfRel());
		return produtoVo;
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping
	public ResponseEntity<?> buscatodos(
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "limite",defaultValue = "5") int limit,
			@RequestParam(value = "order",defaultValue = "asc") String direction){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<ProdutoVO> produtoVo =  service.buscaTodos(pageable);
		produtoVo.stream().forEach(p -> 
		p.add(linkTo(methodOn(ProdutoController.class)
				.buscaPorId(p.getKey())).withSelfRel()));	
		
		PagedModel<?> resources = assembler.toModel(produtoVo);
		
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/busca_por_nome/{nome}")
	public ResponseEntity<?> buscaPorNome(
			@PathVariable("nome") String nome,
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "limite",defaultValue = "5") int limit,
			@RequestParam(value = "order",defaultValue = "asc") String direction){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<ProdutoVO> produtoVo =  service.buscaPorNome(pageable, nome);
		produtoVo.stream().forEach(p -> 
		p.add(linkTo(methodOn(ProdutoController.class)
				.buscaPorId(p.getKey())).withSelfRel()));	
		
		PagedModel<?> resources = assembler.toModel(produtoVo);
		
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoVO salvar(@Valid @RequestBody ProdutoVO produto){
		ProdutoVO produtoVo= service.salvar(produto);
		produtoVo.add(linkTo(methodOn(ProdutoController.class).buscaPorId(produtoVo.getKey())).withSelfRel());
		return produtoVo;
	}
	
	@SuppressWarnings("deprecation")
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutoVO atualizar(@Valid @PathVariable Long id, @RequestBody ProdutoVO produto){
		ProdutoVO produtoVo = service.atualizar(produto, id);
		produtoVo.add(linkTo(methodOn(ProdutoController.class).buscaPorId(id)).withSelfRel());
		return produtoVo;
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ProdutoVO> deletar(@PathVariable Long id){
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
