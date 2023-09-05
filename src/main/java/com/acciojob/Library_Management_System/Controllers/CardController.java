package com.acciojob.Library_Management_System.Controllers;

import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Services.CardService;
import com.acciojob.Library_Management_System.Services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
@Slf4j
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity createCard(@RequestBody LibraryCard card) {
        cardService.createCard(card);
        return new ResponseEntity("Card added successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/issueToStudent")
    public ResponseEntity issueToStudent(@RequestParam ("cardId") Integer Id, @RequestParam ("rollNo") Integer rollNo) {

        try {
            String result = cardService.issueToStudent(Id, rollNo);
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Error in associating card to student", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/totalFine")
    public ResponseEntity getTotalFineForYear2023() {

        try {
            Integer totalFine = transactionService.calculateTotalFineForYear2023();
            return new ResponseEntity(totalFine, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/inactiveStudents")
    public ResponseEntity<List<String>> getInactiveStudents() {

        try {
            List<String> inactiveStudentNames = cardService.getInactiveStudentNames();
            return new ResponseEntity<>(inactiveStudentNames, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
