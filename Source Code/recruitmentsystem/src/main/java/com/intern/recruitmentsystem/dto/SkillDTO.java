package com.fpt.recruitmentsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillDTO {
    private Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;
}
