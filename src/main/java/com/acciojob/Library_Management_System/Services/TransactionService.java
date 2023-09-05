package com.acciojob.Library_Management_System.Services;

import com.acciojob.Library_Management_System.CustomExceptions.BookNotAvailableException;
import com.acciojob.Library_Management_System.CustomExceptions.BookNotFoundException;
import com.acciojob.Library_Management_System.Enums.CardStatus;
import com.acciojob.Library_Management_System.Enums.TransactionStatus;
import com.acciojob.Library_Management_System.Enums.TransactionType;
import com.acciojob.Library_Management_System.Models.Book;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Models.Student;
import com.acciojob.Library_Management_System.Models.Transaction;
import com.acciojob.Library_Management_System.Repositories.BookRepository;
import com.acciojob.Library_Management_System.Repositories.CardRepository;
import com.acciojob.Library_Management_System.Repositories.TransactionRepository;
import com.acciojob.Library_Management_System.ResponseDTOs.MaxFineBookResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private StudentService studentService;

    @Value("${book.maxLimit}")
    private Integer maxBooksLimit;

    public String issueBook(Integer bookId, Integer cardId) throws Exception {

        Transaction transaction = new Transaction(TransactionType.ISSUE, TransactionStatus.PENDING, 0);

        // Book exception handling
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new BookNotFoundException("Book Id is invalid");
        }

        Book book = optionalBook.get();
        if (!book.getIsAvailable()) {
            throw new BookNotAvailableException("Book is not available");
        }

        // Card exception handlin
        Optional<LibraryCard> optionalLibraryCard = cardRepository.findById(cardId);
        if (!optionalLibraryCard.isPresent()) {
            throw new Exception("Card Id is invalid");
        }

        LibraryCard libraryCard = optionalLibraryCard.get();
        if (!libraryCard.getCardStatus().equals(CardStatus.ISSUED, CardStatus.ACTIVE)) {

            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction = transactionRepository.save(transaction);
            throw new Exception("Card is inactive");

        }

        if (libraryCard.getNoOfBooksIssued() == maxBooksLimit) {

            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction = transactionRepository.save(transaction);
            throw new Exception("Book issuing limit has reached");

        }

        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        book.setIsAvailable(Boolean.FALSE);
        libraryCard.setNoOfBooksIssued(libraryCard.getNoOfBooksIssued() + 1);

        transaction.setBook(book);
        transaction.setLibraryCard(libraryCard);

        Transaction transactionWithId = transactionRepository.save(transaction);

        book.getTxnList().add(transactionWithId);
        libraryCard.getTxnList().add(transactionWithId);

        bookRepository.save(book);
        cardRepository.save(libraryCard);

        return "Transaction completed successfully";

    }

    public String returnBook(Integer bookId, Integer cardId) {

        Book book = bookRepository.findById(bookId).get();
        LibraryCard libraryCard = cardRepository.findById(cardId).get();

        List<Transaction> transactionList = transactionRepository.findTransactionsByBookAndLibraryCardAndTransactionStatusAndTransactionType(book, libraryCard, TransactionStatus.SUCCESS, TransactionType.ISSUE);

        Transaction latestTransaction = transactionList.get(transactionList.size() - 1);

        Date issueDate = latestTransaction.getCreatedAt();

        Long milliSecondTime = Math.abs(System.currentTimeMillis() - issueDate.getTime());

        Long DaysAfterIssued = TimeUnit.DAYS.convert(milliSecondTime, TimeUnit.MILLISECONDS);

        Integer fineAmount = 0;

        if (DaysAfterIssued > 5) {
            fineAmount = (int) ((DaysAfterIssued - 5) * 5);
        }

        book.setIsAvailable(Boolean.TRUE);
        libraryCard.setNoOfBooksIssued(libraryCard.getNoOfBooksIssued() - 1);

        Transaction transaction = new Transaction(TransactionType.RETURN, TransactionStatus.SUCCESS, fineAmount);

        transaction.setBook(book);
        transaction.setLibraryCard(libraryCard);

        Transaction transactionWithId = transactionRepository.save(transaction);

        book.getTxnList().add(transactionWithId);
        libraryCard.getTxnList().add(transactionWithId);

        bookRepository.save(book);
        cardRepository.save(libraryCard);

        return "Book returned successfully";

    }

    public Integer calculateTotalFineForYear2023() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfYear2023 = calendar.getTime();

        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date endOfYear2023 = calendar.getTime();

        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startOfYear2023, endOfYear2023);

        int totalFine = 0;

        for (Transaction transaction : transactions) {
            totalFine += transaction.getFineAmount();
        }

        return totalFine;
    }

//    public Integer calculateTotalFineForYear2023() {
//        List<Transaction> transactionsInYear2023 = transactionRepository.findByCreatedAtBetween(
//                getStartOfYear(2023), getEndOfYear(2023)
//        );
//
//        Integer totalFine = 0;
//
//        for (Transaction transaction : transactionsInYear2023) {
//            if (transaction.getTransactionStatus() == TransactionStatus.SUCCESS) {
//                totalFine += transaction.getFineAmount();
//            }
//        }
//
//        return totalFine;
//    }
//
//    private Date getStartOfYear(int year) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return calendar.getTime();
//    }
//
//    private Date getEndOfYear(int year) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
//        calendar.set(Calendar.MILLISECOND, 999);
//        return calendar.getTime();
//    }

    public String findStudentWithMostDistinctBooks() throws Exception {
        List<Student> students = studentService.getAllStudents();

        Map<Integer, Integer> studentDistinctBooksCount = new HashMap<>();

        for (Student student : students) {
            Integer distinctBooksCount = transactionRepository.countDistinctBooksByStudentId(student.getRollNo());
            studentDistinctBooksCount.put(student.getRollNo(), distinctBooksCount);
        }

        // Find student with the most distinct books
        Integer maxDistinctBooksCount = 0;
        Integer studentIdWithMostDistinctBooks = null;

        for (Map.Entry<Integer, Integer> entry : studentDistinctBooksCount.entrySet()) {
            if (entry.getValue() > maxDistinctBooksCount) {
                maxDistinctBooksCount = entry.getValue();
                studentIdWithMostDistinctBooks = entry.getKey();
            }
        }

        if (studentIdWithMostDistinctBooks != null) {
            Optional<Student> student = Optional.ofNullable(studentService.getStudentById(studentIdWithMostDistinctBooks));
            if (student.isPresent()) {
                return student.get().getName();
            }
        }

        return "No student found with the most distinct books.";
    }

    public MaxFineBookResponseDTO getMaxFineBook() {
        List<Transaction> transactions = transactionRepository.findAll();

        if (transactions.isEmpty()) {
            return null;
        }

        Transaction maxFineTransaction = transactions.stream()
                .max(Comparator.comparingInt(Transaction::getFineAmount))
                .orElse(null);

        if (maxFineTransaction == null) {
            return null;
        }

        Book book = maxFineTransaction.getBook();
        String bookTitle = book.getTitle();
        Integer fineAmount = maxFineTransaction.getFineAmount();

        return new MaxFineBookResponseDTO(bookTitle, fineAmount);
    }

}
