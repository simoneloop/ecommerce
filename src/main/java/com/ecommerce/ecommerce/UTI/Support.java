package com.ecommerce.ecommerce.UTI;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.ecommerce.UTI.exception.InsufficientAmountException;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;

import com.ecommerce.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.w3c.dom.html.HTMLTableRowElement;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static com.ecommerce.ecommerce.UTI.Consts.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@UtilityClass
public class Support {


    private Map<String,Double>paymentMap=new HashMap<>();

    static {
        paymentMap.put("user1@",20.0);
        paymentMap.put("user2@",100.0);
        paymentMap.put("user3@",25.0);

    }

    public boolean validateCreditLimit(String email,double paidAmount) throws RuntimeException {
        if(paidAmount>paymentMap.get(email)){
            throw new InsufficientAmountException();
        }
        else{
            return true;
        }
    }


    public Map<String, String> createTokens(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESHTOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        String expires_in=TOKEN_EXPIRES.toString();
        Map<String, String> res = new HashMap<>();
        res.put("access_token", access_token);
        res.put("refresh_token", refresh_token);
        res.put("expires_in", expires_in);
        return res;
    }

    public Map<String, String> createTokens(Users user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESHTOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        String expires_in=TOKEN_EXPIRES.toString();
        Map<String, String> res = new HashMap<>();
        res.put("access_token", access_token);
        res.put("refresh_token", refresh_token);
        res.put("expires_in", expires_in);
        return res;
    }

    public Algorithm defineAlgorith() {
        return Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
    }

    public String tokenGetEmail(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());//giusto rimuovo "Bearer" dal token
                Algorithm algorithm = defineAlgorith();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                return email;
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            return null;
        }
    }
    public String getExceptionName(Exception e){
        return e.getClass().getSimpleName();
    }
}
