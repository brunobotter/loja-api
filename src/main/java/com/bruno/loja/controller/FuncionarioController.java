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

import com.bruno.loja.service.FuncionarioService;
import com.bruno.loja.vo.FuncionarioVO;

@RestController
@RequestMapping("api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	@Autowired
	private FuncionarioService service;
	

	@Autowired
	private PagedResourcesAssembler<FuncionarioVO> assembler;
	
	
	@SuppressWarnings("deprecation")
	@GetMapping("{id}")
	public FuncionarioVO buscaPorId(@PathVariable Long id) {
		FuncionarioVO funcionarioVO = service.buscaPorId(id);
		funcionarioVO.add(linkTo(methodOn(FuncionarioController.class).buscaPorId(id)).withSelfRel());
		return funcionarioVO;
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<FuncionarioVO> funcionarioVO = service.buscaTodosPaginado(pageable);
		funcionarioVO.stream()
				.forEach(p -> p.add(linkTo(methodOn(FuncionarioController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(funcionarioVO);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("busca_por_nome/{nome}")
	public ResponseEntity<?> buscaPorNome(@PathVariable("nome") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<FuncionarioVO> funcionarioVO = service.buscaPorNome(pageable, nome);
		funcionarioVO.stream()
				.forEach(p -> p.add(linkTo(methodOn(FuncionarioController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(funcionarioVO);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	
	@SuppressWarnings("deprecation")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FuncionarioVO salvar(@Valid @RequestBody FuncionarioVO func) {
		FuncionarioVO funcionarioVO = service.salva(func);
		funcionarioVO.add(linkTo(methodOn(ClienteController.class).buscaPorId(func.getKey())).withSelfRel());
		return funcionarioVO;
	}
	
	@SuppressWarnings("deprecation")
	@PutMapping("{id}")
	public FuncionarioVO atualizar(@Valid @PathVariable Long id, @RequestBody FuncionarioVO func){
		FuncionarioVO funcionarioVO = service.atualiza(func, id);
		funcionarioVO.add(linkTo(methodOn(ClienteController.class).buscaPorId(id)).withSelfRel());
		return funcionarioVO;
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<FuncionarioVO> deletar(@PathVariable Long id){
		service.deletaPorId(id);
		return ResponseEntity.noContent().build();
	}
}
