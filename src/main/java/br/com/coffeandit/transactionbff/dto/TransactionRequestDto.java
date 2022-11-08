package br.com.coffeandit.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Objeto de transporte para o envio de uma promessa de transação")
public class TransactionRequestDto extends TransactionDto {

    @JsonIgnore
    private SituacaoEnum situacao;
    @JsonIgnore
    private LocalDateTime data;

    public void naoAnalisada() {
        setSituacao(SituacaoEnum.NAO_ANALISADA);
    }

}
