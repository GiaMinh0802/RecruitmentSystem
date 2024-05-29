package com.fpt.recruitmentsystem.controller.admin;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fpt.recruitmentsystem.dto.SkillDTO;
import com.fpt.recruitmentsystem.service.ISkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admins/skills")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class SkillController {
    private final ISkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAll() {
        return new ResponseEntity<>(skillService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SkillDTO> insert(@RequestBody @Valid SkillDTO newSkill) {
        return new ResponseEntity<>(skillService.insert(newSkill), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<SkillDTO> update(@PathVariable int id, @RequestBody @Valid SkillDTO updatedSkill) {
        return new ResponseEntity<>(skillService.update(id, updatedSkill), HttpStatus.OK);
    }

}
