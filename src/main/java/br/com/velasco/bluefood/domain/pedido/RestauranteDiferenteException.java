package br.com.velasco.bluefood.domain.pedido;

public class RestauranteDiferenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public RestauranteDiferenteException() {
		super();
	}

	public RestauranteDiferenteException(String message) {
		super(message);
	}

}
