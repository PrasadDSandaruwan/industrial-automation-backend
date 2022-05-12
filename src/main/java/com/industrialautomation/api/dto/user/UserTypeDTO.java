package com.industrialautomation.api.dto.user;


import com.industrialautomation.api.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserTypeDTO {

    private long id;
    private String user_type_name;
    private String slug;

    public UserTypeDTO(UserType u) {
        this.id = u.getId();
        this.user_type_name = u.getUser_type_name();
        this.slug = u.getSlug();
    }
}
