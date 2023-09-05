package com.acciojob.Library_Management_System.Controllers;

import com.acciojob.Library_Management_System.Models.Author;
import com.acciojob.Library_Management_System.RequestDTOs.UpdateAuthorNameAndPenNameDTO;
import com.acciojob.Library_Management_System.Services.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@Slf4j
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/add")
    public ResponseEntity addAuthor(@RequestBody Author author) {

        try {
            String result = authorService.addAuthor(author);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }
        catch (Exception e) {
            log.error("Author can not added to the database. {}", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PostMapping("/updateNameAndPenName")
    public String updateNameAndPenName(@RequestBody UpdateAuthorNameAndPenNameDTO request) {

        try {
            String result = authorService.updateNameAndPenName(request);
            return result;
        }
        catch (Exception e) {
            return "Author Id is invalid";
        }

    }

    @GetMapping("/getAuthor")
    public ResponseEntity getAuthorById(@RequestParam ("authorId") Integer Id) {

        try {
            Author author = authorService.getAuthorById(Id);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAuthorGreaterThanAge")
    public List<Author> getAuthorGreaterThanAge(@RequestParam ("age") Integer enteredAge) {
        return authorService.getAuthorGreaterThanAge(enteredAge);
    }

    @GetMapping("/bookTitles")
    public ResponseEntity<List<String>> getBookTitlesByAuthorId(@RequestParam ("authorId") Integer authorId) {

        try {
            List<String> bookTitles = authorService.getBookTitlesByAuthorId(authorId);
            return new ResponseEntity<>(bookTitles, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
