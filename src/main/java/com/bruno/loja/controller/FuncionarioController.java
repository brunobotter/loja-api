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

import com.bruno.loja.model.Funcionario;
import com.bruno.loja.service.FuncionarioService;

@RestController
@RequestMapping("api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;
	

	@Autowired
	private PagedResourcesAssembler<Funcionario> assembler;
	
	
	@GetMapping("{id}")
	public ResponseEntity<Funcionario> buscaPorId(@PathVariable Long id) {
		Funcionario funcionario = funcionarioService.buscaPorId(id);
		return ResponseEntity.ok(funcionario);
	}
	
	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "5") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<Funcionario> funcionario = funcionarioService.buscaTodosPaginado(pageable);
		

		PagedModel<?> resources = assembler.toModel(funcionario);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@GetMapping("/busca_por_nome/")
	public ResponseEntity<?> buscaPorNome(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<Funcionario> funcionario = funcionarioService.buscaPorNome(pageable, nome);
		

		PagedModel<?> resources = assembler.toModel(funcionario);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Funcionario salvar(@Valid @RequestBody Funcionario func) {
		return funcionarioService.salva(func);
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Funcionario> atualizar(@Valid @PathVariable Long id, @RequestBody Funcionario func){
		Funcionario funcionario = funcionarioService.atualiza(func, id);
		return ResponseEntity.ok(funcionario);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Funcionario> deletar(@PathVariable Long id){
		funcionarioService.deletaPorId(id);
		return ResponseEntity.noContent().build();
	}
}
