package br.com.coffeandit.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Getter
@Setter
@ToString
public class BeneficiarioDto implements Serializable {

    @Schema(description = "CPF do beneficiário")
    @NotNull(message = "Informar o CPF")
    private Long cpf;
    @Schema(description = "Código do banco destino")
    @NotNull(message = "Informar código do banco de destino")
    private Long codigoBanco;
    @Schema(description = "Agência de destino")
    @NotNull(message = "Informar agência de destino")
    private String agencia;
    @Schema(description = "Conta de destino")
    @NotNull(message = "Informar a conta de destino")
    private String conta;
    @Schema(description = "Nome do favorecido")
    @NotNull(message = "Informar o nome do favorecido")
    private String nomeFavorecido;
}
