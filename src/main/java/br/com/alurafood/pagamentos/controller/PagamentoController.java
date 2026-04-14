package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PagamentosDto;
import br.com.alurafood.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public Page<PagamentosDto> listar(@PageableDefault(size = 10) Pageable paginacao){
        return service.obterPagamentos(paginacao);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PagamentosDto> detalhar(@PathVariable Long id){
        PagamentosDto dto = service.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PagamentosDto> cadastrar(@RequestBody PagamentosDto dto, UriComponentsBuilder uriBuilder){
        PagamentosDto pagamento = service.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        rabbitTemplate.convertAndSend("pagamentos.ex","",pagamento);
        return ResponseEntity.created(endereco).body(pagamento);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PagamentosDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentosDto dto) {
        PagamentosDto atualizado = service.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable @NotNull Long id){
        service.deletarPagamento(id);
        return ResponseEntity.noContent().build();

    }
    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public ResponseEntity<Void> confirmarPagamento(@PathVariable @NotNull Long id){
        service.confirmarPagamento(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e){
        service.alteraStatus(id);
        return ResponseEntity.ok().build();
    }
}

