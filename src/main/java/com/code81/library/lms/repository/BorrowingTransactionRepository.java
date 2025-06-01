package com.code81.library.lms.repository;

import com.code81.library.lms.entity.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
    List<BorrowingTransaction> findByMember_Id(Long memberId);
    List<BorrowingTransaction> findByBook_Id(Long bookId);
    List<BorrowingTransaction> findByReturned(boolean returned);
}