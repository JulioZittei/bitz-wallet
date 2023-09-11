package br.com.bitz.wallet.controller;

import br.com.bitz.wallet.domain.entity.Account;
import br.com.bitz.wallet.domain.model.request.TransactionDataRequest;
import br.com.bitz.wallet.domain.model.response.TransactionDataResponse;
import br.com.bitz.wallet.service.port.CreateTransactionService;
import br.com.bitz.wallet.service.port.FindTransactionsByPayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final CreateTransactionService createTransactionService;

    private final FindTransactionsByPayerService findTransactionsByPayerService;

    @PostMapping
    public ResponseEntity<TransactionDataResponse> createTransaction(@RequestBody @Valid final TransactionDataRequest requestData,
                                                                     final UriComponentsBuilder uriBuilder) {
        var transaction = createTransactionService.execute(requestData);
        var uri = uriBuilder.path("/api/v1/transactions/{transactionId}").buildAndExpand(transaction.id()).toUri();
        return ResponseEntity.created(uri).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDataResponse>> getTransactionsByPayer() {
        Account accountAuhtenticated = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var transactions = findTransactionsByPayerService.execute(accountAuhtenticated.getId());
        return ResponseEntity.ok(transactions);
    }
}
