package com.acciojob.Library_Management_System.Services;

import com.acciojob.Library_Management_System.Models.Author;
import com.acciojob.Library_Management_System.Models.Book;
import com.acciojob.Library_Management_System.Repositories.AuthorRepository;
import com.acciojob.Library_Management_System.RequestDTOs.UpdateAuthorNameAndPenNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public String addAuthor(Author author) throws Exception {

        if (author.getAuthorId() != null) {
            throw new Exception("Author id must not be sent as parameter.");
        }

        authorRepository.save(author);
        return "Author added successfully";

    }

    public String updateNameAndPenName(UpdateAuthorNameAndPenNameDTO request) throws Exception {

        Optional<Author> optionalAuthor = authorRepository.findById(request.getAuthorId());

        if (!optionalAuthor.isPresent()) {
            throw new Exception("Author id is invalid");
        }

        Author author = optionalAuthor.get();

        author.setName(request.getName());
        author.setPenName(request.getPenName());

        authorRepository.save(author);

        return "Author Name and PenName has been updated";
    }

    public Author getAuthorById(Integer Id) throws Exception {

        Optional<Author> optionalAuthor = authorRepository.findById(Id);

        if (!optionalAuthor.isPresent()) {
            throw new Exception("Author Id is invalid");
        }

        Author author = authorRepository.findById(Id).get();
        return author;

    }

    public List<Author> getAuthorGreaterThanAge(Integer enteredAge) {

        return authorRepository.findAuthorGreaterThanAge(enteredAge);

    }

    public List<String> getBookTitlesByAuthorId(Integer authorId) throws Exception {

        Optional<Author> optionalAuthor = authorRepository.findById(authorId);

        if (!optionalAuthor.isPresent()) {
            throw new Exception("Author Id is invalid");
        }

        Author author = optionalAuthor.get();
        List<String> bookTitles = new ArrayList<>();

        for (Book book : author.getBookList()) {
            bookTitles.add(book.getTitle());
        }

        return bookTitles;
    }

}
