package com.acciojob.Library_Management_System.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MaxFineBookResponseDTO {

    private String bookTitle;
    private Integer fineAmount;
}
