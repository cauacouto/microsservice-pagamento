package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentosDto;
import br.com.alurafood.pagamentos.http.pedidoClient;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.Pedido;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PagamentosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentosRepository repository;

@Autowired
    private  ModelMapper modelMapper;

@Autowired
private pedidoClient pedido;




    public Page<PagamentosDto> obterPagamentos(Pageable paginacao){
        return repository
                .findAll(paginacao)
                .map(p-> modelMapper.map(p, PagamentosDto.class));
    }

    public PagamentosDto obterPorId (Long id){
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("id não encontrado"));

      PagamentosDto dto = modelMapper.map(pagamento, PagamentosDto.class);
      dto.setItens(pedido.obeterItensDoPedido(pagamento.getPedidoId()).getItens());
      return dto;
    }



    public PagamentosDto criarPagamento(PagamentosDto dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentosDto.class);
    }

    public PagamentosDto atualizarPagamento(Long id, PagamentosDto dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentosDto.class);
    }
    public void deletarPagamento(Long id){
        repository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedido.atualizaPagamento(pagamento.get().getPedidoId());
    }

    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());

    }
}
