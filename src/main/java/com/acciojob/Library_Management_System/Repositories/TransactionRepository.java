package com.acciojob.Library_Management_System.Repositories;

import com.acciojob.Library_Management_System.Enums.TransactionStatus;
import com.acciojob.Library_Management_System.Enums.TransactionType;
import com.acciojob.Library_Management_System.Models.Book;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findTransactionsByBookAndLibraryCardAndTransactionStatusAndTransactionType(Book book, LibraryCard libraryCard, TransactionStatus transactionStatus, TransactionType transactionType);

    List<Transaction> findByCreatedAtBetween(Date startDate, Date endDate);

    @Query(value = "SELECT COUNT(DISTINCT t.book) FROM Transaction t WHERE t.libraryCard.student.rollNo = :studentId")
    Integer countDistinctBooksByStudentId(Integer studentId);

}
