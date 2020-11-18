package com.bruno.loja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.loja.model.Descricao;
import com.bruno.loja.service.OsService;

@RestController
@RequestMapping("api/comentario")
@CrossOrigin(origins = "*")
public class ComentarioController {

	@Autowired
	private OsService osService;
	
	@PutMapping("/os/{osId}")
	@ResponseStatus(HttpStatus.OK)
	public Descricao adicionar(@Valid @PathVariable Long osId, @RequestBody Descricao desc ) {
		Descricao descricao = osService.adicionarComentario(desc, osId);
		return descricao;
	}
	
	@GetMapping
	public List<Descricao> buscaComentarios(){
		return osService.buscaComentarios();
	}
	
	@GetMapping("os/{idOs}")
	public List<Descricao> buscaPorIdOs(@PathVariable Long idOs){
		return osService.buscaComentariosPorOs(idOs);
	}
	
	@DeleteMapping("os/comentario/{idComentario}")
	public ResponseEntity<Descricao> deletar(@PathVariable Long idComentario){
		osService.deletarComentario(idComentario);
		return ResponseEntity.noContent().build();
	}
}
