package com.industrialautomation.api.dto.user;

import com.industrialautomation.api.model.User;
import com.industrialautomation.api.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserDetailsDTO {

    private Long id;

    private String first_name;

    private String last_name;

    private String email;

    private String nic;

    private LocalDate birthday;

    private String contact_no;

    private String profile_picture;

    private LocalDateTime deleted;

    private LocalDateTime force_password_change_flag;

    private LocalDateTime added_at;

    private UserTypeDTO user_type;

    public UserDetailsDTO(User u){ this.id = id;
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
        this.email = u.getEmail();
        this.nic = u.getNic();
        this.birthday = u.getBirthday();
        this.contact_no = u.getContact_no();
        this.profile_picture = u.getProfile_picture();
        this.deleted = u.getDeleted();
        this.force_password_change_flag = u.getForce_password_change_flag();
        this.added_at = u.getAdded_at();
        this.user_type = new UserTypeDTO( u.getUserType());
    }
}
