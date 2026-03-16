package br.com.alurafood.pagamentos.repository;

import br.com.alurafood.pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentosRepository extends JpaRepository<Pagamento,Long> {
}
