package com.bruno.loja.controller;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

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

import com.bruno.loja.service.ClienteService;
import com.bruno.loja.vo.ClienteVO;

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
	private ClienteService service;

	@Autowired
	private PagedResourcesAssembler<ClienteVO> assembler;
	
	@SuppressWarnings("deprecation")
	@GetMapping("{id}")
	public ClienteVO buscaPorId(@PathVariable Long id) {
		ClienteVO cliente = service.buscaPorId(id);
		cliente.add(linkTo(methodOn(ProdutoController.class).buscaPorId(id)).withSelfRel());
		return cliente;
	}

	@SuppressWarnings("deprecation")
	@GetMapping("busca_por_nome/{nome}")
	public ResponseEntity<?> buscaPorNome(@PathVariable("nome") String nome,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limite,
			@RequestParam(value = "order", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limite, Sort.by(sortDirection, "nome"));

		Page<ClienteVO> clienteVO = service.buscaPorNome(pageable, nome);
		clienteVO.stream()
				.forEach(p -> p.add(linkTo(methodOn(ClienteController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(clienteVO);

		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@SuppressWarnings("deprecation")
	@GetMapping
	public ResponseEntity<?> buscaTodos(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limite", defaultValue = "2") int limit,
			@RequestParam(value = "order", defaultValue = "asc") String direction) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));

		Page<ClienteVO> clienteVO = service.buscaTodosPaginado(pageable);
		clienteVO.stream()
				.forEach(p -> p.add(linkTo(methodOn(ClienteController.class)
						.buscaPorId(p.getKey())).withSelfRel()));

		PagedModel<?> resources = assembler.toModel(clienteVO);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}


	@GetMapping("gerarpdf")
	public void generatePDF(HttpServletResponse response) throws Exception {

		List<ClienteVO> clientes = service.buscaTodos();
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

	@SuppressWarnings("deprecation")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteVO salvar(@Valid @RequestBody ClienteVO cli) {
		ClienteVO clienteVO = service.salvar(cli);
		clienteVO.add(linkTo(methodOn(ClienteController.class).buscaPorId(cli.getKey())).withSelfRel());
		return clienteVO;
	}

	@SuppressWarnings("deprecation")
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ClienteVO atualizar(@Valid @PathVariable Long id, @RequestBody ClienteVO cli) {
		ClienteVO clienteVo = service.atualiza(cli, id);
		clienteVo.add(linkTo(methodOn(ClienteController.class).buscaPorId(id)).withSelfRel());
		return clienteVo;
	}

	@DeleteMapping("{id}")
	public ResponseEntity<ClienteVO> deletar(@PathVariable Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
