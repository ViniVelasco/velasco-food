package br.com.velasco.bluefood.application.domain.pagamento;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.velasco.bluefood.application.pagamento.BandeiraCartao;
import br.com.velasco.bluefood.domain.pedido.Pedido;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@NotNull
	@OneToOne
	@MapsId
	private Pedido pedido;
	
	@NotNull
	private LocalDateTime data;
	
	@Column(name = "num_cartao_final")
	@NotNull
	@Size(min = 4, max = 4)
	private String numCartaoFinal;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private BandeiraCartao bandeiraCartao;
	
	public void definirNumeroEBandeira(String numCartao) {
		numCartaoFinal = numCartao.substring(12);
		bandeiraCartao = getBandeira(numCartao);
	}
	
	private BandeiraCartao getBandeira(String numCartao) {
		if(numCartao.startsWith("0000")) {
			return BandeiraCartao.AMEX;
		} else if(numCartao.startsWith("1111")) {
			return BandeiraCartao.ELO;
		} else if(numCartao.startsWith("2222")) {
			return BandeiraCartao.MASTER;
		}
		
		return BandeiraCartao.VISA;
	}
	
	
}
