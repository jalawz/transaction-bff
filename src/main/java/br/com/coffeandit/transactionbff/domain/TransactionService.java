package br.com.coffeandit.transactionbff.domain;

import br.com.coffeandit.transactionbff.dto.TransactionDto;
import br.com.coffeandit.transactionbff.dto.TransactionRequestDto;
import br.com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRedisRepository transactionRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Optional<TransactionDto> save(final TransactionRequestDto dto) {
        dto.setData(LocalDateTime.now());
        return Optional.of(transactionRedisRepository.save(dto));
    }

    public Optional<TransactionDto> findById(final String id) {
        return transactionRedisRepository.findById(id);
    }
}
