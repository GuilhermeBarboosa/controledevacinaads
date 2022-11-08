package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import web.controlevacinacao.validation.IntegerAttributesRelation;
import web.controlevacinacao.validation.util.AttributesRelation;

@Entity
@Table(name = "lote")
@DynamicUpdate
@IntegerAttributesRelation(attribute1 = "nroDosesDoLote",
                           relation = AttributesRelation.GREATEROREQUAL,
                           attribute2 = "nroDosesAtual")
public class Lote implements Serializable {

	private static final long serialVersionUID = -3935828642122652510L;

	@Id
	@SequenceGenerator(name = "gerador2", sequenceName = "lote_codigo_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador2", strategy = GenerationType.SEQUENCE)
	private Long codigo;
	@NotNull(message = "A validade é obrigatória.")
	private LocalDate validade;
	@Column(name = "nro_doses_do_lote")
	@NotNull(message = "O número de doses do lote é obrigatório.")
	@Min(value = 0, message = "O número de doses do lote tem que ser maior ou igual a 0")
	private Integer nroDosesDoLote;
	@Column(name = "nro_doses_atual")
	@NotNull(message = "O número de doses atual é obrigatório.")
	@Min(value = 0, message = "O número de doses do lote tem que ser maior ou igual a 0")
	private Integer nroDosesAtual;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_vacina")
	@Valid
	@NotNull(message = "A vacina é obrigatória.")
	private Vacina vacina;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public Integer getNroDosesDoLote() {
		return nroDosesDoLote;
	}

	public void setNroDosesDoLote(Integer nroDosesDoLote) {
		this.nroDosesDoLote = nroDosesDoLote;
	}

	public Integer getNroDosesAtual() {
		return nroDosesAtual;
	}

	public void setNroDosesAtual(Integer nroDosesAtual) {
		this.nroDosesAtual = nroDosesAtual;
	}

	public Vacina getVacina() {
		return vacina;
	}

	public void setVacina(Vacina vacina) {
		this.vacina = vacina;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lote other = (Lote) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lote [codigo=" + codigo + ", validade=" + validade + ", nroDosesDoLote=" + nroDosesDoLote
				+ ", nroDosesAtual=" + nroDosesAtual + ", vacina=" + vacina + ", status=" + status + "]";
	}

//	@Override
//	public String toString() {
//		return "codigo: " + codigo + "\nvalidade: " + validade + "\nnroDosesDoLote: " + nroDosesDoLote
//				+ "\nnroDosesAtual: " + nroDosesAtual;
//	}
	
	

}
