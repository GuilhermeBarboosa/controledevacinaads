package web.controlevacinacao.model.filter;

import java.time.LocalDate;

public class PessoaFilter {

	private Long codigo;
	private String nome;
	private String cpf;
	private LocalDate dataNascimento;
	private Long codigoProfissao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Long getCodigoProfissao() {
		return codigoProfissao;
	}

	public void setCodigoProfissao(Long codigoProfissao) {
		this.codigoProfissao = codigoProfissao;
	}

}
