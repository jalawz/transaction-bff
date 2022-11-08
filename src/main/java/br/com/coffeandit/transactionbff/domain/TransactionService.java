package br.com.coffeandit.transactionbff.domain;

import br.com.coffeandit.transactionbff.dto.TransactionDto;
import br.com.coffeandit.transactionbff.dto.TransactionRequestDto;
import br.com.coffeandit.transactionbff.exception.NotFoundException;
import br.com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRedisRepository transactionRedisRepository;
    private final RetryTemplate retryTemplate;
    private final ReactiveKafkaProducerTemplate<String, TransactionRequestDto> reactiveKafkaProducerTemplate;

    @Value("${app.topic}")
    private String topic;

    @Transactional
    @Retryable(value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public Mono<TransactionRequestDto> save(final TransactionRequestDto dto) {

        return Mono.fromCallable(() -> {
            log.info("Salvando no Redis");
            dto.setData(LocalDateTime.now());
            dto.naoAnalisada();
            return transactionRedisRepository.save(dto);
        }).doOnError(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new NotFoundException(throwable.getMessage());
        })
        .doOnSuccess(transaction -> {
            log.info("Transação persistida com sucesso {}", transaction);
            reactiveKafkaProducerTemplate.send(topic, dto)
                .doOnSuccess(voidSenderResult -> log.info(voidSenderResult.toString()))
                .subscribe();
        })
        .doFinally(signalType -> {
            if (signalType.compareTo(SignalType.ON_COMPLETE) == 0) 
                log.info("Mensagem enviada para o Kafka com sucesso 2 {}", dto);
        });

    }

    public Optional<TransactionDto> findById(final String id) {
        return retryTemplate.execute(retry -> {
            log.info("Consultando Redis");
            return transactionRedisRepository.findById(id);
        });
    }
}
