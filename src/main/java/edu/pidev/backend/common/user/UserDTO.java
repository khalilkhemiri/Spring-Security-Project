package edu.pidev.backend.common.user;

import edu.pidev.backend.common.enumuration.Language;
import edu.pidev.backend.common.enumuration.TunisianCity;
import edu.pidev.backend.common.enumuration.UserGender;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    long id;
    String username;
    String email;
    String firstName;
    String lastName;
    String fullName;
    UserGender gender;
    LocalDate birthDate;
    TunisianCity addressCity;
    String badge;
    Language lang;
    String address;
    String mobile;
    String phone;
    List<String> roles;
    boolean showEmail;
    boolean showMobile;
    boolean showPhone;
    boolean showBadge;
    int profileCompilation;
}
