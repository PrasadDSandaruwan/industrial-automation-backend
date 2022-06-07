package com.industrialautomation.api.dto.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.industrialautomation.api.model.Auth;
import com.industrialautomation.api.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserEditDTO {
    private String first_name;
    private String last_name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
    private String contact_no;

}
