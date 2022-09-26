package br.com.coffeandit.transactionbff.feign;

import br.com.coffeandit.transactionbff.dto.LimiteDiario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "limites", url = "${limites.url}")
public interface LimiteClient {

    @GetMapping(path = "/limite-diario/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE)
    LimiteDiario buscarLimiteDiario(
            @PathVariable("agencia") final Long agencia,
            @PathVariable("conta") final Long conta
    );
}
