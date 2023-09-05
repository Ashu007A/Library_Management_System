package com.acciojob.Library_Management_System.Services;

import com.acciojob.Library_Management_System.Enums.Genre;
import com.acciojob.Library_Management_System.Models.Author;
import com.acciojob.Library_Management_System.Models.Book;
import com.acciojob.Library_Management_System.Models.Student;
import com.acciojob.Library_Management_System.Repositories.AuthorRepository;
import com.acciojob.Library_Management_System.Repositories.BookRepository;
import com.acciojob.Library_Management_System.RequestDTOs.AddBookRequestDTO;
import com.acciojob.Library_Management_System.ResponseDTOs.BookResponseDTO;
import com.acciojob.Library_Management_System.ResponseDTOs.MostPopularAuthorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public String addBook(AddBookRequestDTO requestDTO) throws Exception {

        Optional<Author> optionalAuthor = authorRepository.findById(requestDTO.getAuthorId());

        if (!optionalAuthor.isPresent()) {
            throw new Exception("Author id is invalid.");
        }

        Author author = optionalAuthor.get();

        Book book = new Book(requestDTO.getTitle(), requestDTO.isAvailable(), requestDTO.getGenre(), requestDTO.getPublicationDate(), requestDTO.getPrice());

//        book.setIsAvailable(Boolean.TRUE);

        book.setAuthor(author);
        author.getBookList().add(book);

        authorRepository.save(author);

        return "Book has been added successfully.";

    }

    public List<BookResponseDTO> getBookByGenre(Genre genre) {

        List<Book> bookList = bookRepository.findBooksByGenre(genre);
        List<BookResponseDTO> bookResponseDTOList = new ArrayList<>();

        for (Book book : bookList) {
            BookResponseDTO bookResponseDto = new BookResponseDTO(book.getBookId(), book.getTitle(), book.getIsAvailable(), book.getGenre(), book.getPublicationDate(), book.getPrice(), book.getAuthor().getName());

            bookResponseDTOList.add(bookResponseDto);
        }

        return bookResponseDTOList;

    }

    public MostPopularAuthorResponseDTO getMostPopularAuthor() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            return null;
        }

        Map<Author, Set<Student>> authorToStudentsMap = new HashMap<>();

        for (Book book : books) {
            Author author = book.getAuthor();
            Student student = book.getTxnList().stream()
                    .map(transaction -> transaction.getLibraryCard().getStudent())
                    .findFirst().orElse(null);

            if (author != null && student != null) {
                authorToStudentsMap.computeIfAbsent(author, k -> new HashSet<>()).add(student);
            }
        }

        Map.Entry<Author, Set<Student>> mostPopularEntry = authorToStudentsMap.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().size()))
                .orElse(null);

        if (mostPopularEntry == null) {
            return null;
        }

        String authorName = mostPopularEntry.getKey().getName();
        Integer popularity = mostPopularEntry.getValue().size();

        return new MostPopularAuthorResponseDTO(authorName, popularity);
    }

}
