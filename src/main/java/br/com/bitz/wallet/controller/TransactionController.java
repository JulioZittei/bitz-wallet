package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.service.port.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDataResponse> createTransaction(@RequestBody @Valid final TransactionDataRequest requestData,
                                                                     final UriComponentsBuilder uriBuilder) {
        var transaction = transactionService.execute(requestData);
        var uri = uriBuilder.path("/api/v1/transactions/{transactionId}").buildAndExpand(transaction.id()).toUri();
        return ResponseEntity.created(uri).body(transaction);
    }
}
