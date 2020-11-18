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

import com.bruno.loja.model.OS;
import com.bruno.loja.service.OsService;

@RestController
@RequestMapping("api/os")
@CrossOrigin(origins = "*")
public class OsController {

	@Autowired
	private OsService osService;
	
	@Autowired
	private PagedResourcesAssembler<OS> assembler;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<OS> buscaPorId(@PathVariable Long id) {
		OS os =  osService.buscarPorId(id);
		return ResponseEntity.ok(os);
	}
	
	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<OS> os = osService.buscaTodosPaginado(pageable);
		

		PagedModel<?> resources = assembler.toModel(os);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@GetMapping("busca_por_nome/")
	public ResponseEntity<?> buscaTodosPorNomeCliente(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<OS> os = osService.buscaPorNomeCliente(pageable, nome);
		
		PagedModel<?> resources = assembler.toModel(os);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<OS> deletar(@PathVariable Long id){
		osService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<OS> adicionar(@Valid @RequestBody OS os) {
		OS ordem =  osService.salvar(os);
		return ResponseEntity.ok(ordem);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OS> atualizar(@PathVariable Long id, @Valid @RequestBody OS os){
		OS ordem =  osService.atualizar(os, id);
		return ResponseEntity.ok(ordem);
	}
	
	@PutMapping("/{osId}/finalizada")
	@ResponseStatus(HttpStatus.OK)
	public void finalizarOs(@PathVariable Long osId) {
		osService.finalizarOs(osId);
	}
	

}
