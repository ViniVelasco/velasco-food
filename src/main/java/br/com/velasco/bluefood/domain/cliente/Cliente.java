package br.com.velasco.bluefood.domain.cliente;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import br.com.velasco.bluefood.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
public class Cliente extends Usuario {
	
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "O CPF n�o pode ser vazio")
	@Pattern(regexp = "[0-9]{11}", message="O CPF possui formato inv�lido")
	@Column(length = 11, nullable = false)
	private String cpf;
	
	@NotBlank(message = "O CEP n�o pode ser vazio")
	@Pattern(regexp = "[0-9]{8}", message="O CEP possui formato inv�lido")
	@Column(length = 8)
	private String cep;
	
	public String getFormattedCep() {
		return cep.substring(0, 5) + "-" + cep.substring(5);
	}

}
