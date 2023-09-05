package com.acciojob.Library_Management_System.Controllers;

import com.acciojob.Library_Management_System.Enums.Department;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Models.Student;
import com.acciojob.Library_Management_System.Services.StudentService;
import com.acciojob.Library_Management_System.Services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity addStudent(@RequestBody Student student) {

        try {
            String result = studentService.addStudent(student);
            return new ResponseEntity("Student added successfully.", HttpStatus.CREATED);
        }
        catch (Exception e) {
            log.error("Student not added {}", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/findDptById")
    public ResponseEntity findDptById(@RequestParam ("Id") Integer Id) {

        try {
            Department department = studentService.findDptById(Id);
            return new ResponseEntity(department, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Department not found/Invalid request {}", e.getMessage());
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/cardStatus")
    public ResponseEntity<String> getCardStatusByRollNo(@RequestParam ("rollNo") Integer rollNo) {

        try {
            LibraryCard libraryCard = studentService.getCardStatusByRollNo(rollNo);
            String cardStatus = libraryCard != null ? libraryCard.getCardStatus().toString() : "Card not found";
            return new ResponseEntity<>(cardStatus, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mostDistinctBooks")
    public ResponseEntity findStudentWithMostDistinctBooks() {
        try {
            String studentName = transactionService.findStudentWithMostDistinctBooks();
            return new ResponseEntity(studentName, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
