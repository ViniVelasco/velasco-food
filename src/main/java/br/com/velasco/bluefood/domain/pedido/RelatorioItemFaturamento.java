package br.com.velasco.bluefood.domain.pedido;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class RelatorioItemFaturamento {

	private String nome;
	private Long quantidade;
	private BigDecimal valor;
}
