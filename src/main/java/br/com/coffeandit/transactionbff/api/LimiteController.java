package br.com.coffeandit.transactionbff.api;

import br.com.coffeandit.transactionbff.dto.LimiteDiario;
import br.com.coffeandit.transactionbff.feign.LimiteClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limites")
@RequiredArgsConstructor
public class LimiteController {

    private final LimiteClient limiteClient;

    @GetMapping("/{agencia}/{conta}")
    public LimiteDiario buscarLimiteDiario(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta) {
        return limiteClient.buscarLimiteDiario(agencia, conta);
    }
}