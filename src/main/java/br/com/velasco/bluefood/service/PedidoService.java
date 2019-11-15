package br.com.velasco.bluefood.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.velasco.bluefood.application.domain.pagamento.Pagamento;
import br.com.velasco.bluefood.application.pagamento.DadosCartao;
import br.com.velasco.bluefood.application.pagamento.PagamentoRepository;
import br.com.velasco.bluefood.application.pagamento.StatusPagamento;
import br.com.velasco.bluefood.domain.pedido.Carrinho;
import br.com.velasco.bluefood.domain.pedido.ItemPedido;
import br.com.velasco.bluefood.domain.pedido.ItemPedidoPK;
import br.com.velasco.bluefood.domain.pedido.ItemPedidoRepository;
import br.com.velasco.bluefood.domain.pedido.Pedido;
import br.com.velasco.bluefood.domain.pedido.PedidoRepository;
import br.com.velasco.bluefood.domain.pedido.Pedido.Status;
import br.com.velasco.bluefood.util.SecurityUtils;
@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Value("${bluefood.sbpay.url}")
	private String sbpayUrl;

	@Value("${bluefood.sbpay.token}")
	private String sbPaytoken;

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = PagamentoException.class)
	public Pedido criarEPagar(Carrinho carrinho, String numCartao) throws PagamentoException {
		Pedido pedido = new Pedido();
		pedido.setData(LocalDateTime.now());
		pedido.setCliente(SecurityUtils.loggedCliente());
		pedido.setRestaurante(carrinho.getRestaurante());
		pedido.setStatus(Status.Producao);
		pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
		pedido.setSubtotal(carrinho.getPrecoTotal(false));
		pedido.setTotal(carrinho.getPrecoTotal(true));
		pedido = pedidoRepository.save(pedido);

		int ordem = 1;

		for (ItemPedido itemPedido : carrinho.getItens()) {
			itemPedido.setId(new ItemPedidoPK(pedido, ordem++));
			itemPedidoRepository.save(itemPedido);
		}

		DadosCartao dadosCartao = new DadosCartao();
		dadosCartao.setNumCartao(numCartao);

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Token", sbPaytoken);

		HttpEntity<DadosCartao> requestEntity = new HttpEntity<>(dadosCartao, headers);

		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> response;
		try {
			response = restTemplate.postForObject(sbpayUrl, requestEntity, Map.class);
		} catch (Exception e) {
			throw new PagamentoException("Erro no servidor de pagamento");
		}
		StatusPagamento statusPagamento = StatusPagamento.valueOf(response.get("status"));

		if (statusPagamento != StatusPagamento.Autorizado) {
			throw new PagamentoException(statusPagamento.getDescricao());
		}
		
		Pagamento pagamento = new Pagamento();
		pagamento.setData(LocalDateTime.now());
		pagamento.setPedido(pedido);
		pagamento.definirNumeroEBandeira(numCartao);
		pagamentoRepository.save(pagamento);

		return pedido;
	}

}
