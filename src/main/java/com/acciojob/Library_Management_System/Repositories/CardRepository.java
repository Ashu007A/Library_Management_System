package com.acciojob.Library_Management_System.Repositories;

import com.acciojob.Library_Management_System.Enums.CardStatus;
import com.acciojob.Library_Management_System.Models.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<LibraryCard, Integer> {

    List<LibraryCard> findAllByCardStatusNotIn(CardStatus... cardStatuses);

}
