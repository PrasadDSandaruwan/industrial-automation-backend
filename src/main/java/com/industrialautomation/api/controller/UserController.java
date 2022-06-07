package com.industrialautomation.api.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.dto.user.UserEditDTO;
import com.industrialautomation.api.model.User;
import com.industrialautomation.api.service.UserService;
import com.industrialautomation.api.utilities.AccessTokenHandler;
import com.industrialautomation.api.utilities.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        try{
            if (principal == null)
                return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
            String email = accessTokenHandler.getEmailByPrincipal(principal);

            if (email == null)
                return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

            return userService.getUserDetails(email);

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");
        }

    }

    @GetMapping("/v1/user/force-password-change")
    public Object forcePasswordChange(Principal principal ){

        try{
            if (principal == null)
                return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
            String user_id = accessTokenHandler.getEmailByPrincipal(principal);

            if (user_id == null)
                return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

            return userService.forcePasswordChange(user_id);

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");
        }


    }

    @PostMapping("/v1/user/change-password")
    public Object changePassword(Principal principal, @RequestBody JsonNode jsonNode){
        try{
            if (principal == null)
                return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
            String email = accessTokenHandler.getEmailByPrincipal(principal);

            if (email == null)
                return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

            String new_password = (jsonNode.hasNonNull("new_password")) ? jsonNode.get("new_password").asText(): null;
            String old_password = (jsonNode.hasNonNull("old_password")) ? jsonNode.get("old_password").asText(): null;

            if(old_password==null || new_password == null)
                return new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return userService.changePassword(email,new_password,old_password);

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");
        }

    }

    @PostMapping("/v1/user/first-time-change-password")
    public Object firstChangePassword(Principal principal, @RequestBody JsonNode jsonNode){

        try{
            if (principal == null)
                return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
            String  email = accessTokenHandler.getEmailByPrincipal(principal);

            if (email == null)
                return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

            String new_password = (jsonNode.hasNonNull("new_password")) ? jsonNode.get("new_password").asText(): null;


            if(new_password == null)
                return new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return userService.firstChangePassword(email,new_password);

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.FAILED,"Operation Failed.");
        }

    }

    @PostMapping("/v1/user/update-profile")
    public Object updateUserProfile(Principal principal, @RequestBody JsonNode jsonNode){
        try{
            if (principal == null)
                return  new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Token not found");
            String email = accessTokenHandler.getEmailByPrincipal(principal);

            if (email == null)
                return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid Token");

            if (!(jsonNode.hasNonNull("first_name") && jsonNode.hasNonNull("last_name") &&
                  jsonNode.hasNonNull("contact_no")
            ))
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Not all required parameters were present in the Request.");

            String first_name, last_name, contact_no, birthday;


            try{
                first_name = jsonNode.get("first_name").asText();
                last_name = jsonNode.get("last_name").asText();
                contact_no = jsonNode.get("contact_no").asText();
                birthday = jsonNode.get("birthday").asText();
            }catch (Exception e) {
                e.printStackTrace();
                return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Inputs are not valid.");
            }



            return  userService.editUserProfile(email,first_name,last_name,contact_no,birthday);

        }catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.FAILED,"Operation Failed.");
        }



    }

    @PostMapping("/v1/user/get-all-users")
    public Object getAllUsers(@RequestBody JsonNode jsonNode){
        try {
            Integer limit = (jsonNode.hasNonNull("limit")) ? jsonNode.get("limit").asInt() : null;
            Integer offset = (jsonNode.hasNonNull("offset")) ? jsonNode.get("offset").asInt() : null;

            return userService.getAllUsers(limit, offset);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.FAILED,"Operation Failed.");
        }
    }

    @DeleteMapping("/v1/user/delete/{user_id}")
    public Object deleteUser(@PathVariable Long user_id){
        try {
            return userService.deleteUser(user_id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201,ResponseStatus.FAILED,"Operation Failed.");
        }
    }

    @GetMapping("/v1/user/unique/{slug}")
    public Object checkUnique(@PathVariable String slug){
        if (slug == null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Alarm slug missing");

        try {
            return userService.checkUnique(slug);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}
