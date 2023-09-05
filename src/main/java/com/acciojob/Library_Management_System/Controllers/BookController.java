package com.acciojob.Library_Management_System.Controllers;

import com.acciojob.Library_Management_System.Enums.Genre;
import com.acciojob.Library_Management_System.RequestDTOs.AddBookRequestDTO;
import com.acciojob.Library_Management_System.ResponseDTOs.BookResponseDTO;
import com.acciojob.Library_Management_System.ResponseDTOs.MostPopularAuthorResponseDTO;
import com.acciojob.Library_Management_System.Services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity addBook(@RequestBody AddBookRequestDTO requestDTO) {

        try {
            String result = bookService.addBook(requestDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (Exception e) {
            log.error("Book can not be added to the database. {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/getByGenre")
    public ResponseEntity getBookByGenre(@RequestParam ("genre") Genre genre) {

        List<BookResponseDTO> responseDtoList = bookService.getBookByGenre(genre);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);

    }

    @GetMapping("/mostPopularAuthor")
    public ResponseEntity<MostPopularAuthorResponseDTO> getMostPopularAuthor() {
        MostPopularAuthorResponseDTO responseDTO = bookService.getMostPopularAuthor();
        if (responseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
