package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvDTO {
    private Integer id;
    private Date creatdDate;
    private String fileName;
    private String linkCv;
    private Integer candidateId;
    private String data;
}
