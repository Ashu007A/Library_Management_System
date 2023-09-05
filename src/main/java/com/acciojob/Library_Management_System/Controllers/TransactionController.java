package com.acciojob.Library_Management_System.Controllers;

import com.acciojob.Library_Management_System.ResponseDTOs.MaxFineBookResponseDTO;
import com.acciojob.Library_Management_System.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/issueBook")
    public ResponseEntity issueBook(@RequestParam ("bookId") Integer bookId, @RequestParam ("cardId") Integer cardId) {

        try {
            String result = transactionService.issueBook(bookId, cardId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/returnBook")
    public ResponseEntity returnBook(@RequestParam ("bookId") Integer bookId, @RequestParam ("cardId") Integer cardId) {

        try {
            String result = transactionService.returnBook(bookId, cardId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/maxFineBook")
    public ResponseEntity<MaxFineBookResponseDTO> getMaxFineBook() {
        MaxFineBookResponseDTO responseDTO = transactionService.getMaxFineBook();
        if (responseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
