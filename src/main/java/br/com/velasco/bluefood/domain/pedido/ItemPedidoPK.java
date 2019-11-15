package br.com.velasco.bluefood.domain.pedido;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ItemPedidoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne
	private Pedido pedido;
	
	@NotNull
	private Integer ordem;
}
