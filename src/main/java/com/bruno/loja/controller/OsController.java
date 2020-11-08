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

import com.bruno.loja.model.OS;
import com.bruno.loja.service.OsService;
import com.bruno.loja.vo.OsVO;

@RestController
@RequestMapping("api/os")
@CrossOrigin(origins = "*")
public class OsController {

	@Autowired
	private OsService osService;
	
	@Autowired
	private PagedResourcesAssembler<OsVO> assembler;
	
	
	@SuppressWarnings("deprecation")
	@GetMapping("/{id}")
	public OsVO buscaPorId(@PathVariable Long id) {
		OsVO os =  osService.buscarPorId(id);
		os.add(linkTo(methodOn(OsController.class).buscaPorId(id)).withSelfRel());
		return os;
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<OsVO> osVo = osService.buscaTodosPaginado(pageable);
		osVo.stream()
				.forEach(p -> p.add(linkTo(methodOn(OsController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(osVo);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("busca_por_nome/{nome}")
	public ResponseEntity<?> buscaTodosPorNomeCliente(@PathVariable("nome") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "20") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

		Page<OsVO> osVo = osService.buscaPorNomeCliente(pageable, nome);
		osVo.stream()
				.forEach(p -> p.add(linkTo(methodOn(OsController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(osVo);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<OsVO> deletar(@PathVariable Long id){
		osService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OsVO adicionar(@Valid @RequestBody OS os) {
		OsVO osVO =  osService.salvar(os);
		osVO.add(linkTo(methodOn(OsController.class).buscaPorId(os.getId())).withSelfRel());
		return osVO;
	}
	
	@SuppressWarnings("deprecation")
	@PutMapping()
	@ResponseStatus(HttpStatus.OK)
	public OsVO atualizar(@Valid @RequestBody OS os){
		OsVO osVO =  osService.atualizar(os);
		osVO.add(linkTo(methodOn(OsController.class).buscaPorId(os.getId())).withSelfRel());
		return osVO;
	}
	
	@PutMapping("/{osId}/finalizada")
	@ResponseStatus(HttpStatus.OK)
	public void finalizarOs(@PathVariable Long osId) {
		osService.finalizarOs(osId);
	}
}
