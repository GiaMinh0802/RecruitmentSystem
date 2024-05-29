package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.dto.LevelDTO;
import com.fpt.recruitmentsystem.service.ILevelService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/admins/levels")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class LevelController {
    private final ILevelService levelService;

    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAll() {
        return new ResponseEntity<>(levelService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LevelDTO> insert(@RequestBody @Valid LevelDTO newLevel) {
        return new ResponseEntity<>(levelService.insert(newLevel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LevelDTO> update(@PathVariable int id, @RequestBody @Valid LevelDTO updatedLevel) {
        return new ResponseEntity<>(levelService.update(id, updatedLevel), HttpStatus.OK);
    }
}
