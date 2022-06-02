package com.ecommerce.ecommerce.controllers;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.services.ProductService;
import com.ecommerce.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/modify")
    public ResponseEntity modifyProduct(@RequestBody Product p){
        try{
            if((int)p.getQuantity()==-1){
                productService.deleteProduct(p);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(productService.modifyProduct(p),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        try{
            return new ResponseEntity(productService.getAll(),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getProductPageable")
    public ResponseEntity getPageableProductFiltered(@RequestParam(required = false) String typo,@RequestParam(required = false) String ordered,@RequestParam(required = false) String page,@RequestParam(required = false) String pageSize,@RequestParam(required = false) String field){
        try{
            return new ResponseEntity(productService.getProductFilteredAndPageabled(typo,ordered,page,pageSize,field),HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
