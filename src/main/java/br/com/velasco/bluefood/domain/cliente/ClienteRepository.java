package br.com.velasco.bluefood.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	public Cliente findByEmail(String email);
	
}
