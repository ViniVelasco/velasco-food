package br.com.velasco.bluefood.application.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.velasco.bluefood.application.domain.pagamento.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
	
}
