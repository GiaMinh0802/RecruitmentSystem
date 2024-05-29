package com.fpt.recruitmentsystem.controller.admin;

import com.fpt.recruitmentsystem.dto.PositionDTO;
import com.fpt.recruitmentsystem.service.IPositionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/admins/positions")
@Tag(name = "Admin",description = "Admin Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class PositionController {
    private final IPositionService positionService;

    @GetMapping
    public ResponseEntity<List<PositionDTO>> getAll() {
        return new ResponseEntity<>(positionService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PositionDTO> insert(@RequestBody @Valid PositionDTO newPosition) {
        return new ResponseEntity<>(positionService.insert(newPosition), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionDTO> update(@PathVariable int id, @RequestBody @Valid PositionDTO updatedPosition) {
        return new ResponseEntity<>(positionService.update(id, updatedPosition), HttpStatus.OK);
    }
}
