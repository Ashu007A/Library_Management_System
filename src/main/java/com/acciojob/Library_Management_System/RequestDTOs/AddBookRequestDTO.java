package com.acciojob.Library_Management_System.RequestDTOs;

import com.acciojob.Library_Management_System.Enums.Genre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddBookRequestDTO {

    private String title;

    private boolean isAvailable;

    private Genre genre;

    private Date publicationDate;

    private Integer price;

    private Integer authorId;

}
