package web.controlevacinacao.model.filter;

import java.time.LocalDate;

public class AplicacaoFilter {

	private Long codigo;
	private LocalDate data;
	private Long codigoPessoa;
	private Long codigoLote;
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Long getCodigoPessoa() {
		return codigoPessoa;
	}
	public void setCodigoPessoa(Long codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}
	public Long getCodigoLote() {
		return codigoLote;
	}
	public void setCodigoLote(Long codigoLote) {
		this.codigoLote = codigoLote;
	}

}
