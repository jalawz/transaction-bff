package br.com.coffeandit.transactionbff.api;

import br.com.coffeandit.transactionbff.domain.TransactionService;
import br.com.coffeandit.transactionbff.dto.TransactionDto;
import br.com.coffeandit.transactionbff.dto.TransactionRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
@Tag(name = "/transaction", description = "Grupo de API's para manipulação de transações financeiras")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(description = "API para criar uma transação financeira.")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> sendTransaction(@RequestBody final TransactionRequestDto transactionRequestDto) {
        Optional<TransactionDto> transactionDto = transactionService.save(transactionRequestDto);
        if (transactionDto.isPresent()) {
            return Mono.just(transactionDto.get());
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save resource");
    }

    @Operation(description = "API para buscar os transações por id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorno OK da Lista de transações"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @GetMapping(value = "/{id}")
    public Mono<TransactionDto> getTransaction(@PathVariable("id") final String uuid) {
        Optional<TransactionDto> transactionDto = transactionService.findById(uuid);
        if (transactionDto.isPresent()) {
            return Mono.just(transactionDto.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
    }

    @Operation(description = "API para remover as transações persistidas")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Retorno OK da remoçao"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> deleteTransaction(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }

    @Operation(description = "API para autorizar a transaçao financeira")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorno OK da remoçao"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @PatchMapping(value = "/{id}/confirm")
    public Mono<TransactionDto> confirmTransaction(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }
}
