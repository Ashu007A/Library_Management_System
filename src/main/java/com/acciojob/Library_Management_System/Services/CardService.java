package com.acciojob.Library_Management_System.Services;

import com.acciojob.Library_Management_System.Enums.CardStatus;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import com.acciojob.Library_Management_System.Models.Student;
import com.acciojob.Library_Management_System.Repositories.CardRepository;
import com.acciojob.Library_Management_System.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private StudentRepository studentRepository;

    public String createCard(LibraryCard card) {
        cardRepository.save(card);
        return "Card added successfully";
    }

    public String issueToStudent(Integer Id, Integer rollNo) throws Exception {

        if(!studentRepository.existsById(rollNo)) {
            throw new Exception("Student Roll number is invalid");
        }

        if (!cardRepository.existsById(Id)) {
            throw new Exception("Card number is invalid");
        }

        Optional<Student> optionalStudent = studentRepository.findById(rollNo);
        Student student = optionalStudent.get();

        Optional<LibraryCard> optionalLibraryCard = cardRepository.findById(Id);
        LibraryCard libraryCard = optionalLibraryCard.get();

        libraryCard.setCardStatus(CardStatus.ISSUED);

        // Bi-directional updation
        libraryCard.setStudent(student);
        student.setLibraryCard(libraryCard);

//        cardRepository.save(libraryCard);
        studentRepository.save(student);

        return "Student - card association is successful";

    }

    public List<String> getInactiveStudentNames() {
        List<String> inactiveStudentNames = new ArrayList<>();

        List<LibraryCard> inactiveCards = cardRepository.findAllByCardStatusNotIn(CardStatus.ISSUED, CardStatus.ACTIVE);

        for (LibraryCard card : inactiveCards) {
            if (card.getStudent() != null) {
                inactiveStudentNames.add(card.getStudent().getName());
            }
        }

        return inactiveStudentNames;
    }

}
