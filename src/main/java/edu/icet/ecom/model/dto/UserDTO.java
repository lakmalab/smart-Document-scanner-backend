package edu.icet.ecom.model.dto;

import edu.icet.ecom.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String profilePicturePath;
    private String name;
    private String email;
    private String address;
    private String contactNumber;
    private String city;
    private String province;
    private UserRole role;
}
