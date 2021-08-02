package com.example.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path="/demo")
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody void addNewUser (@RequestParam String name,
                                          @RequestParam String email,
                                          @RequestParam String password,
                                          HttpServletResponse response) throws IOException {

        User n = new User();
        n.setName(name);
        n.setEmail(email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        n.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        userRepository.save(n);
        response.sendRedirect("http://localhost:8080");
    }

    @PostMapping(path="/auth")
    public @ResponseBody String authUser (@RequestParam String email, @RequestParam String password,
                                        HttpServletResponse response) throws IOException {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        String str = userRepository.findByEmail(email).getPassword();
        str = str + '/' + BCrypt.hashpw(password, BCrypt.gensalt());

//        if (userRepository.findByEmail(email).getPassword() == hashedPassword)
//        {
//            response.sendRedirect("http://localhost:8080/Table");
//        }
        //response.sendRedirect("http://localhost:8080");
        return str;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}