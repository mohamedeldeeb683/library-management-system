package com.code81.library.lms.service;

import com.code81.library.lms.entity.BorrowingTransaction;
import com.code81.library.lms.entity.Book;
import com.code81.library.lms.entity.Member;
import com.code81.library.lms.entity.AppUser;
import com.code81.library.lms.repository.BorrowingTransactionRepository;
import com.code81.library.lms.repository.BookRepository;
import com.code81.library.lms.repository.MemberRepository;
import com.code81.library.lms.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BorrowingTransactionService {
    private static final Logger logger = LoggerFactory.getLogger(BorrowingTransactionService.class);

    private final BorrowingTransactionRepository borrowingTransactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public BorrowingTransactionService(BorrowingTransactionRepository borrowingTransactionRepository,
                                       BookRepository bookRepository,
                                       MemberRepository memberRepository,
                                       AppUserRepository appUserRepository) {
        this.borrowingTransactionRepository = borrowingTransactionRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<BorrowingTransaction> findAllBorrowingTransactions() {
        return borrowingTransactionRepository.findAll();
    }

    public Optional<BorrowingTransaction> findBorrowingTransactionById(Long id) {
        return borrowingTransactionRepository.findById(id);
    }

    @Transactional
    public BorrowingTransaction borrowBook(Long bookId, Long memberId, Long appUserId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new IllegalArgumentException("App User not found"));

        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book is not available for borrowing");
        }

        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusWeeks(2));
        transaction.setAppUser(appUser);

        book.setAvailable(false);
        bookRepository.save(book);
        return borrowingTransactionRepository.save(transaction);
    }

    @Transactional
    public BorrowingTransaction returnBook(Long transactionId) {
        BorrowingTransaction transaction = borrowingTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (transaction.isReturned()) {
            throw new IllegalArgumentException("Book already returned");
        }

        transaction.setReturnDate(LocalDate.now());
        transaction.setReturned(true);

        Book book = transaction.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowingTransactionRepository.save(transaction);
    }

    public BorrowingTransaction updateBorrowingTransaction(BorrowingTransaction transactionDetails) {
        return borrowingTransactionRepository.findById(transactionDetails.getId())
                .map(existingTransaction -> {
                    existingTransaction.setBorrowDate(transactionDetails.getBorrowDate());
                    existingTransaction.setDueDate(transactionDetails.getDueDate());
                    existingTransaction.setReturnDate(transactionDetails.getReturnDate());
                    existingTransaction.setReturned(transactionDetails.isReturned());

                    if (transactionDetails.getAppUser() != null && transactionDetails.getAppUser().getId() != null) {
                        appUserRepository.findById(transactionDetails.getAppUser().getId())
                                .ifPresent(existingTransaction::setAppUser);
                    }
                    return borrowingTransactionRepository.save(existingTransaction);
                })
                .orElse(null);
    }

    public void deleteBorrowingTransaction(Long id) {
        borrowingTransactionRepository.deleteById(id);
    }
}