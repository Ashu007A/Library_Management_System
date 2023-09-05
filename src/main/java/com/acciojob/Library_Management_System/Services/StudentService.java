package com.acciojob.Library_Management_System.Services;

import com.acciojob.Library_Management_System.Enums.Department;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Models.Student;
import com.acciojob.Library_Management_System.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public String addStudent(Student student) throws Exception {

        if (student.getRollNo() != null) {
            throw new Exception("Roll number should not be sent as a parameter.");
        }

        studentRepository.save(student);

        return "Student added successfully.";
    }

    public Department findDptById(Integer rollNo) throws Exception {
        Optional<Student> optionalStudent = studentRepository.findById(rollNo);

        if (!optionalStudent.isPresent()) {
            throw new Exception("Roll number is invalid.");
        }

        Student student = optionalStudent.get();

        return student.getDepartment();
    }

    public LibraryCard getCardStatusByRollNo(Integer rollNo) throws Exception {

        Optional<Student> optionalStudent = studentRepository.findById(rollNo);

        if (!optionalStudent.isPresent()) {
            throw new Exception("Roll number is invalid.");
        }

        Student student = optionalStudent.get();
        LibraryCard libraryCard = student.getLibraryCard();

        if (libraryCard == null) {
            throw new Exception("No library card associated with the student.");
        }

        return libraryCard;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer rollNo) throws Exception {
        Optional<Student> optionalStudent = studentRepository.findById(rollNo);
        if (!optionalStudent.isPresent()) {
            throw new Exception("Student with Roll Number " + rollNo + " not found");
        }
        return optionalStudent.get();
    }

}
