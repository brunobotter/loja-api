package com.bruno.loja.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.bruno.loja.model.StatusVenda;
import com.bruno.loja.model.Venda;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@JsonPropertyOrder({ "id", "dataVenda", "cliente", "funcionario", "listaItem", "desconto", "valorTotal",
		"statusVenda" })
public class VendaVO extends RepresentationModel<VendaVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapping("id")
	@JsonProperty("id")
	protected Long key;

	private LocalDate dataVenda;

	private ClienteVO cliente;

	private FuncionarioVO funcionario;

	private List<ItemVO> listaItem = new ArrayList<ItemVO>();

	private BigDecimal desconto;

	private BigDecimal valorTotal;

	private StatusVenda statusVenda;

	public VendaVO() {

	}

	public VendaVO(Venda venda) {
		this.key = venda.getId();
		this.dataVenda = venda.getDataVenda();
		this.desconto = venda.getDesconto();

		this.valorTotal = venda.getValorTotal();
		this.statusVenda = venda.getStatusVenda();
		if (venda.getCliente() != null) {
			this.cliente = new ClienteVO(venda.getCliente());
		}
		if (venda.getFuncionario() != null) {
			this.funcionario = new FuncionarioVO(venda.getFuncionario());
		}
		if(this.listaItem != null) {
		this.listaItem = venda.getListaItem().stream()
				.map(v -> new ItemVO(v)).collect(Collectors.toList());
		}
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public LocalDate getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(LocalDate dataVenda) {
		this.dataVenda = dataVenda;
	}

	public ClienteVO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteVO cliente) {
		this.cliente = cliente;
	}

	public FuncionarioVO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public List<ItemVO> getListaItem() {
		return listaItem;
	}

	public void setListaItem(List<ItemVO> listaItem) {
		this.listaItem = listaItem;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public StatusVenda getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(StatusVenda statusVenda) {
		this.statusVenda = statusVenda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result + ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result + ((funcionario == null) ? 0 : funcionario.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((listaItem == null) ? 0 : listaItem.hashCode());
		result = prime * result + ((statusVenda == null) ? 0 : statusVenda.hashCode());
		result = prime * result + ((valorTotal == null) ? 0 : valorTotal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendaVO other = (VendaVO) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (funcionario == null) {
			if (other.funcionario != null)
				return false;
		} else if (!funcionario.equals(other.funcionario))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (listaItem == null) {
			if (other.listaItem != null)
				return false;
		} else if (!listaItem.equals(other.listaItem))
			return false;
		if (statusVenda != other.statusVenda)
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		return true;
	}

	

}
