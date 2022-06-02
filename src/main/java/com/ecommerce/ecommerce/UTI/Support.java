package com.ecommerce.ecommerce.UTI;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static com.ecommerce.ecommerce.UTI.Consts.*;

@Service
public class Support {



    public static Map<String,String> createTokens(User user, HttpServletRequest request){
        Algorithm algorithm=Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
        String access_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+TOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+REFRESHTOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String,String> res=new HashMap<>();
        res.put("access_token",access_token);
        res.put("refresh_token",refresh_token);
        return res;
    }
    public static Map<String,String> createTokens(Users user, HttpServletRequest request){
        Algorithm algorithm=Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
        String access_token= JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+TOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token= JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+REFRESHTOKEN_EXPIRES))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String,String> res=new HashMap<>();
        res.put("access_token",access_token);
        res.put("refresh_token",refresh_token);
        return res;
    }
    public static Algorithm defineAlgorith(){
        return Algorithm.HMAC256(ALGHORITM_SECRET.getBytes());
    }
}
