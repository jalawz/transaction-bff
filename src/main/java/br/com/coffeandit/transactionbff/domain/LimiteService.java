package br.com.coffeandit.transactionbff.domain;

import br.com.coffeandit.transactionbff.dto.LimiteDiario;
import br.com.coffeandit.transactionbff.feign.LimiteClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LimiteService {

    private final LimiteClient limiteClient;
    private final CircuitBreaker timeCircuitBreaker;

    public Mono<LimiteDiario> buscarLimiteDiario(final Long agencia, final Long conta) {
        return buscarLimiteDiarioSupplier(agencia, conta);
    }

    private Mono<LimiteDiario> buscarLimiteDiarioSupplier(final Long agencia, final Long conta) {
        var limiteDiarioSup =
                timeCircuitBreaker.decorateSupplier(() -> limiteClient.buscarLimiteDiario(agencia, conta));

        return Mono.fromSupplier(
            Decorators.ofSupplier(limiteDiarioSup)
            .withCircuitBreaker(timeCircuitBreaker)
            .withFallback(Arrays.asList(CallNotPermittedException.class), e -> this.getStaticLimit())
            .decorate()
        );

        
    }

    private LimiteDiario getStaticLimit() {
        return LimiteDiario.builder()
                .id(1l)
                .valor(BigDecimal.TEN)
                .agencia(200l)
                .conta(200l)
                .build();
    }
}
