package com.tesis.urbe.user.controller;

import java.util.List;

import com.tesis.urbe.user.dto.DeleteUserDTO;
import com.tesis.urbe.user.dto.UpdateUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tesis.urbe.user.dto.UserDTO;
import com.tesis.urbe.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // Inyección por constructor (evita usar @Autowired aquí, es más limpio)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserById/{idUsuario}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("idUsuario") Integer idUsuario) {
        UserDTO user = userService.getUserById(idUsuario);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        UpdateUserDTO updateUser = userService.updateUser(updateUserDTO);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<DeleteUserDTO> deleteUser(@RequestBody DeleteUserDTO deleteUserDTO) {
        DeleteUserDTO deleteUser = userService.deleteUser(deleteUserDTO);
        return ResponseEntity.ok(deleteUser);
    }
}