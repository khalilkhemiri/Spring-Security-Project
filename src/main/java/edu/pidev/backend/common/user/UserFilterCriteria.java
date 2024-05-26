package edu.pidev.backend.common.user;

import edu.pidev.backend.common.enumuration.TunisianCity;
import edu.pidev.backend.common.enumuration.UserBadge;
import edu.pidev.backend.common.enumuration.UserGender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterCriteria {
    String search;
    UserBadge badge;
    TunisianCity city;
    UserGender gender;
    boolean accountLocked;
    boolean enabled;
}
