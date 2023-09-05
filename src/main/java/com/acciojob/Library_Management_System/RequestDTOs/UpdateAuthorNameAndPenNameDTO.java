package com.acciojob.Library_Management_System.RequestDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAuthorNameAndPenNameDTO {

    private Integer authorId;

    private String name;

    private String penName;

}
