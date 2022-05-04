package com.industrialautomation.api.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.User;
import com.industrialautomation.api.service.UserService;
import com.industrialautomation.api.utilities.AccessTokenHandler;
import com.industrialautomation.api.utilities.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenHandler accessTokenHandler;


    @PostMapping("/v1/user/add-user")
    public Object addUser(@RequestBody JsonNode jsonNode){

        if (!(jsonNode.hasNonNull("first_name") && jsonNode.hasNonNull("last_name") &&
                jsonNode.hasNonNull("email") && jsonNode.hasNonNull("contact_no") &&
                jsonNode.hasNonNull("nic") && jsonNode.hasNonNull("type_id") ))
            return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Not all required parameters were present in the Request.");

        String first_name, last_name, email, contact_no, nic;
        long type_id;

        try{
            first_name = jsonNode.get("first_name").asText();
            last_name = jsonNode.get("last_name").asText();
            email = jsonNode.get("email").asText();
            contact_no = jsonNode.get("contact_no").asText();
            nic = jsonNode.get("nic").asText();
            type_id = jsonNode.get("type_id").asLong();

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");
        }

        if(!InputValidator.validateEmail(email))
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");

        return  userService.addUser(
                first_name, last_name, email, contact_no, nic, type_id
        );

    }

    @GetMapping("/v1/user/user-details")
    public Object getUserDetails(Principal principal){
        if (principal == null)
            return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
         Long user_id = accessTokenHandler.getIdByPrincipal(principal);

         if (user_id == null)
             return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

         return userService.getUserDetails(user_id);
    }
}
