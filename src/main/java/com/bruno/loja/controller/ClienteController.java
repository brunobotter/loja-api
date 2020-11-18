package com.bruno.loja.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

import com.bruno.loja.model.Cliente;
import com.bruno.loja.service.ClienteService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("api/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private PagedResourcesAssembler<Cliente> assembler;
	
	@GetMapping("{id}")
	public ResponseEntity<Cliente> buscaPorId(@PathVariable Long id) {
		Cliente cliente = clienteService.buscaPorId(id);
		return ResponseEntity.ok(cliente);
	}

	@GetMapping("/busca_por_nome/")
	public ResponseEntity<?> buscaPorNome(
			@RequestParam(value = "nome",defaultValue = "") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limite,
			@RequestParam(value = "order", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limite, Sort.by(sortDirection, "nome"));

		Page<Cliente> cliente = clienteService.buscaPorNome(pageable, nome);
		

		PagedModel<?> resources = assembler.toModel(cliente);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limit,
			@RequestParam(value = "order", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<Cliente> cliente = clienteService.buscaTodosPaginado(pageable);
		

		PagedModel<?> resources = assembler.toModel(cliente);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}


	@GetMapping("gerarpdf")
	public void generatePDF(HttpServletResponse response) throws Exception {

		List<Cliente> clientes = clienteService.buscaTodos();
		String path = "C:\\Users\\servidor\\Desktop\\Nova pasta";
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/cliente.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();

		response.setContentType("application/pdf");
		response.addHeader("Content-disposition", "attachment; filename=cliente.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
		JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\cliente.pdf");
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Cliente> salvar(@Valid @RequestBody Cliente cli) {
		Cliente cliente = clienteService.salvar(cli);
		return ResponseEntity.ok(cliente);
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long id, @RequestBody Cliente cli) {
		Cliente cliente = clienteService.atualiza(cli, id);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Cliente> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
