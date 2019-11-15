package br.com.velasco.bluefood.application.pagamento;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DadosCartao {
	
	@Pattern(regexp = "\\d{16}", message = "O n�mero do cart�o � inv�lido")
	@NotNull
	private String numCartao;
	
	public String getNumCartao() {
		return numCartao;
	}
	
	public void setNumCartao(String numCartao) {
		this.numCartao = numCartao;
	}

}
