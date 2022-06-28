package com.ecommerce.ecommerce.controllers;

import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.services.ProductService;
import com.ecommerce.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody Product p){
        try{
            return new ResponseEntity(productService.addProduct(p), HttpStatus.OK);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/modify")
    public ResponseEntity modifyProduct(@RequestBody Product p,@RequestParam String oldName){
        try{
            return new ResponseEntity(productService.modifyProduct(p,oldName),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/modifyHots")
    public ResponseEntity modifyHotProduct(@RequestBody Map<String,Boolean> map){
        try{
            productService.modifyHotProduct(map);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/deleteProducts")
    public ResponseEntity deleteProducts(@RequestBody Collection<String> products){
        try{
            productService.deleteProducts(products);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        try{
            return new ResponseEntity(productService.getAll(),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getProductPageable")
    public ResponseEntity getPageableProductFiltered(@RequestParam(required = false) String typo,@RequestParam(required = false) String ordered,@RequestParam(required = false) String page,@RequestParam(required = false) String pageSize,@RequestParam(required = false) String field){
        try{
            return new ResponseEntity(productService.getProductFilteredAndPageabled(typo,ordered,page,pageSize,field),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/buy")
    public ResponseEntity buyProduct(HttpServletRequest request, @RequestParam String productName, @RequestParam String quantity){
        try{

            String email= Support.tokenGetEmail(request);
            log.info("email: "+email+" ciao");
            productService.buyProduct(email,productName,quantity);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/buyMyCart")
    public ResponseEntity buyMyCart(HttpServletRequest request){
        try{
            String email=Support.tokenGetEmail(request);
            productService.buyMyCart(email);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getHotProduct")
    public ResponseEntity getHotProduct(@RequestParam boolean isHot){
        try{
            return new ResponseEntity(productService.getAllHotProducts(isHot),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/getProduct")
    public ResponseEntity getProduct(@RequestParam String name){
        try{
            return new ResponseEntity(productService.getProduct(name),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(Support.getExceptionName(exception),HttpStatus.BAD_REQUEST);
        }

    }
}
