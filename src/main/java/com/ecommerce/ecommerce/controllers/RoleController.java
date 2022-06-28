package com.ecommerce.ecommerce.controllers;


import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity saveRole(@RequestBody Role role){
        try{
            return new ResponseEntity(userService.saveRole(role), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @Data
    class RoleToUserForm{
        private String email;
        private String roleName;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/addtouser")
    public ResponseEntity addRoleToUser(@RequestBody RoleToUserForm rtu){
        try{
            userService.addRoleToUser(rtu.getEmail(),rtu.getRoleName());
            return new ResponseEntity( HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }


}
