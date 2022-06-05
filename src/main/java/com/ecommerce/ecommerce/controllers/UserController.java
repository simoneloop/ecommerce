package com.ecommerce.ecommerce.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.entities.UsersDTO;
import com.ecommerce.ecommerce.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    public ResponseEntity saveUser(@RequestBody Users users){
        try{
            return new ResponseEntity(userService.saveUser(users), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        return new ResponseEntity(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader=request.getHeader(AUTHORIZATION);
        if( authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());//giusto rimuovo "Bearer" dal token
                Algorithm algorithm=Support.defineAlgorith();//TODO refactor
                JWTVerifier verifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT=verifier.verify(refresh_token);
                String email=decodedJWT.getSubject();
                Users user =userService.getUser(email);
                Map<String,String> tokens= Support.createTokens(user,request);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }
            catch (Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                /*response.sendError(FORBIDDEN.value());*/
                //mando l'errore come json creato da me
                Map<String,String> error=new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
        else{
            throw new RuntimeException("Refresh token is missing");
        }

    }

    @PostMapping("/addToCart")
    public ResponseEntity addToCart(HttpServletRequest request,@RequestParam String productName,@RequestParam String quantity){
        try{
            String email=Support.tokenGetEmail(request);
            return new ResponseEntity(userService.addToCart(email,productName,quantity), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/removeFromCart")
    public ResponseEntity removeFromCart(HttpServletRequest request,@RequestParam String productName){
        try{
            String email=Support.tokenGetEmail(request);
            return new ResponseEntity(userService.removeFromCart(email,productName), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getUserCart")
    public ResponseEntity getUserCart(HttpServletRequest request){
        try{
            String email=Support.tokenGetEmail(request);
            return new ResponseEntity(userService.getUserCart(email),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/modifyMyDetails")
    public ResponseEntity modifyMyDetails(HttpServletRequest request,@RequestBody UsersDTO details){
        try{
            String email=Support.tokenGetEmail(request);
            return new ResponseEntity(userService.modifyMyDetails(email,details),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getMyDetails")
    public ResponseEntity getMyDetails(HttpServletRequest request){
        try{
            String email=Support.tokenGetEmail(request);
            return new ResponseEntity(userService.getMyDetails(email),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity( Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }


}
