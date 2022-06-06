package com.ecommerce.ecommerce.controllers;


import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.repositories.UserRepository;
import com.ecommerce.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    @Autowired
    UserService userService;

    @GetMapping("/getMyOrders")
    public ResponseEntity getMyOrders(HttpServletRequest request){
        try{
            String email= Support.tokenGetEmail(request);
            //TODO
            return new ResponseEntity(userService.getMyOrders(email),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
}
