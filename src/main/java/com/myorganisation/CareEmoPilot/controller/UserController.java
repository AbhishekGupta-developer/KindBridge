package com.myorganisation.CareEmoPilot.controller;

import com.myorganisation.CareEmoPilot.dto.request.UserRequestDTO;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDTO;
import com.myorganisation.CareEmoPilot.dto.response.UserResponseDTO;
import com.myorganisation.CareEmoPilot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.updateUser(id, userRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponseDTO> removeUser(@PathVariable Long id) {
        GenericResponseDTO genericResponseDTO = userService.removeUser(id);
        return new ResponseEntity<>(genericResponseDTO, (genericResponseDTO.isSuccess()) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
