package com.ecommerce.ecommerce;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.services.ProductService;
import com.ecommerce.ecommerce.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Bean
    CommandLineRunner run(UserService userService, ProductService productService){
        return args -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));


            userService.saveUser(new Users(null,"simone","lopez","admin","3517204474","viagBruno5","admin","img1.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","user","mannaiaal numero","asd","user","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email1","numero1","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email2","numero2","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email3","numero3","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));


            userService.addRoleToUser("admin","ROLE_ADMIN");
            /*userService.addRoleToUser("user","ROLE_USER");
            userService.addRoleToUser("email1","ROLE_USER");
            userService.addRoleToUser("email2","ROLE_USER");
            userService.addRoleToUser("email3","ROLE_USER");*/
            /*productService.addProduct(new Product(null,"collanagioiosa","una gioiosa collana",2,(float)3,"collana","https://i.ibb.co/w6MwjbJ/collana-gioiosa.jpg",null,false,true));
            productService.addProduct(new Product(null,"collanasfarzosa","una sfarzosa collana",4,(float)4,"collana","https://i.ibb.co/THT1bzq/collana-sfarzosa.jpg",null,false,true));
            productService.addProduct(new Product(null,"collanagraziosa","una graziosa collana",15,(float)5,"collana","https://i.ibb.co/0f1pmdF/collana-graziosa.jpg",null,true,true));
            productService.addProduct(new Product(null,"bracciale gioioso","un gioioso bracciale",21,(float)3.5,"bracciale","https://i.ibb.co/MyTZ2bM/bracciale-gioioso.jpg",null,true,true));
            productService.addProduct(new Product(null,"bracciale sfarzoso","uno sfarzoso bracciale",21,(float)4.5,"bracciale","https://i.ibb.co/ydSbfLn/bracciale-sfarzoso.jpg",null,true,true));
            productService.addProduct(new Product(null,"bracciale grazioso","un grazioso bracciale",21,(float)5.5,"bracciale","https://i.ibb.co/3prXJQ8/bracciale-grazioso.jpg",null,true,true));
            productService.addProduct(new Product(null,"orecchino gioioso","un gioioso orecchino",21,(float)3.6,"orecchino","https://i.ibb.co/gSBCdYH/orecchino-gioioso.jpg",null,true,true));
            productService.addProduct(new Product(null,"orecchino sfarzoso","uno sfarzoso orecchino",21,(float)4.6,"orecchino","https://i.ibb.co/NF2xphm/orecchino-sfarzoso.jpg",null,true,true));
            productService.addProduct(new Product(null,"orecchino grazioso","un grazioso orecchino",21,(float)5.6,"orecchino","https://i.ibb.co/YRGMngS/orecchino-grazioso.jpg",null,true,true));
*/
            productService.addProduct(new Product(null,"Digivice","Si narra trasportasse in altri mondi, ma sembra solo essere un pezzo di plastica",2,(float)3,"utilita","https://i.ibb.co/pKqpTjP/digi.jpg",null,false,true));
            productService.addProduct(new Product(null,"Goku","Una statuetta raffigurante un antico guerriero",4,(float)4,"estetica","https://i.ibb.co/5Rt4XWy/goku.jpg",null,false,true));
            productService.addProduct(new Product(null,"Jack","Un portachiavi fortunato, forse",15,(float)5,"utilita","https://i.ibb.co/KX8M8s3/jack.jpg",null,true,true));
            productService.addProduct(new Product(null,"Keyblade","Sembra essere una chiave, ma non apre nessuna porta",21,(float)3.5,"estetica","https://i.ibb.co/xSSY8HG/keyblade.jpg",null,true,true));
            productService.addProduct(new Product(null,"Mudkip culturista","Ad alcuni piace mettersi in mostra",21,(float)4.5,"estetica","https://i.ibb.co/m9L9HRz/marble.jpg",null,true,true));
            productService.addProduct(new Product(null,"Antico meccanismo 1","Per essere utilizzabile dovrebbe essere pieno almeno al 70%",21,(float)5.5,"speciale","https://i.ibb.co/CnYb8cQ/meccanismo1.jpg",null,true,true));
            productService.addProduct(new Product(null,"Antico meccanismo 2","Molto semplice e affidabile meglio del suo predecessore",21,(float)3.6,"utilità","https://i.ibb.co/ZxZbczd/meccanismo2.jpg",null,true,true));
            productService.addProduct(new Product(null,"Medaglione con lupo","Un grosso medaglione raffigurante la testa di un lupo, abbastanza ingombrante",21,(float)4.6,"estetica","https://i.ibb.co/T0qbg9L/medaglione.jpg",null,true,true));
            productService.addProduct(new Product(null,"Rotocoso","Da qualche parte nel mondo uno sta ancora girando",21,(float)5.6,"speciale","https://i.ibb.co/GsYjVfR/rotocoso.jpg",null,true,true));




        };
    }
}



