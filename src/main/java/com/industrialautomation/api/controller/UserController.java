package com.industrialautomation.api.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/v1/add-user")
    public Object addUser(@RequestBody JsonNode jsonNode){
        String name = (jsonNode.hasNonNull("name") )?  jsonNode.get("name").asText(): null;
        String role = (jsonNode.hasNonNull("role") )?  jsonNode.get("role").asText(): null;
        String email = (jsonNode.hasNonNull("email") )?  jsonNode.get("email").asText(): null;

        return  userService.addUser(name,email,role);

    }
}
