package com.code81.library.lms.controller;

import com.code81.library.lms.entity.BorrowingTransaction;
import com.code81.library.lms.service.BorrowingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/borrowings")
public class BorrowingTransactionController {
    private static final Logger logger = LoggerFactory.getLogger(BorrowingTransactionController.class);

    private final BorrowingTransactionService borrowingTransactionService;

    @Autowired
    public BorrowingTransactionController(BorrowingTransactionService borrowingTransactionService) {
        this.borrowingTransactionService = borrowingTransactionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    public ResponseEntity<List<BorrowingTransaction>> getAllBorrowingTransactions() {
        List<BorrowingTransaction> transactions = borrowingTransactionService.findAllBorrowingTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    public ResponseEntity<BorrowingTransaction> getBorrowingTransactionById(@PathVariable Long id) {
        Optional<BorrowingTransaction> transaction = borrowingTransactionService.findBorrowingTransactionById(id);
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/borrow")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    public ResponseEntity<BorrowingTransaction> borrowBook(@RequestBody BorrowingTransaction transaction) {
        try {
            BorrowingTransaction newTransaction = borrowingTransactionService.borrowBook(
                    transaction.getBook().getId(),
                    transaction.getMember().getId(),
                    transaction.getAppUser().getId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/return/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    public ResponseEntity<BorrowingTransaction> returnBook(@PathVariable Long transactionId) {
        try {
            BorrowingTransaction returnedTransaction = borrowingTransactionService.returnBook(transactionId);
            return ResponseEntity.ok(returnedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<BorrowingTransaction> updateBorrowingTransaction(@PathVariable Long id, @RequestBody BorrowingTransaction transactionDetails) {
        transactionDetails.setId(id);
        BorrowingTransaction updatedTransaction = borrowingTransactionService.updateBorrowingTransaction(transactionDetails);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteBorrowingTransaction(@PathVariable Long id) {
        borrowingTransactionService.deleteBorrowingTransaction(id);
        return ResponseEntity.noContent().build();
    }
}