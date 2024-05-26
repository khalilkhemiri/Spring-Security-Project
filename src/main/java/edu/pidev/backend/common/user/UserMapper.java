package edu.pidev.backend.common.user;

import edu.pidev.backend.auth.RegistrationRequest;
import edu.pidev.backend.entity.Role;
import edu.pidev.backend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserMapper {
//    public Picture toUser(Request request) {
//        return Picture.builder()
//                .id(request.id())
//                .title(request.title())
//                .build();
//    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .addressCity(user.getAddressCity())
                .address(user.getAddress())
                .badge(user.getBadge().getDisplayName())
                .lang(user.getLang())
                .mobile(user.getMobile())
                .phone(user.getPhone())
                .roles(user.
                        getRoles().
                        stream().
                        map(Role::getName)
                        .toList()
                ).profileCompilation(user.getProfileCompilation())
                .showEmail(user.isShowEmail())
                .showMobile(user.isShowMobile())
                .showPhone(user.isShowPhone())
                .showBadge(user.isShowBadge())
                .build();
    }

    // Simplified method to map UserDTO to User entity (update operation)
    public static User toUser(UserDTO userDTO, User existingUser) {

        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setGender(userDTO.getGender());
        existingUser.setBirthDate(userDTO.getBirthDate());
        existingUser.setAddressCity(userDTO.getAddressCity());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setMobile(userDTO.getMobile());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setLang(existingUser.getLang());

        return existingUser;
    }

    public static User toUser(UpdateProfileRequest request, User existingUser) {
        log.info("Mapping UpdateProfileRequest to User");

        // Update fields in existingUser based on non-null values from request
        if (request.getId() > 0) {
            existingUser.setId(request.getId());
        }

        if (request.getFirstName() != null) {
            existingUser.setFirstName(request.getFirstName());
        }

        // lastName is optional (can be null)
        existingUser.setLastName(request.getLastName());

        if (request.getGender() != null) {
            existingUser.setGender(request.getGender());
        }

        if (request.getBirthDate() != null) {
            existingUser.setBirthDate(request.getBirthDate());
        }

        if (request.getAddressCity() != null) {
            existingUser.setAddressCity(request.getAddressCity());
        }

        if (request.getLang() != null) {
            existingUser.setLang(request.getLang());
        }

        // address is optional (can be null)
        existingUser.setAddress(request.getAddress());

        if (request.getMobile() != null) {
            existingUser.setMobile(request.getMobile());
        }
        // phone is optional (can be null)
        existingUser.setPhone(request.getPhone());

        existingUser.setShowEmail(request.isShowEmail());
        existingUser.setShowMobile(request.isShowMobile());
        existingUser.setShowPhone(request.isShowPhone());
        existingUser.setShowBadge(request.isShowBadge());

        log.info("Mapping completed successfully");
        return existingUser;
    }


    public static User mapRegistrationRequestToUser(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getEmail()); // Generate username based on first name and last name
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setGender(request.getGender()); // Set default gender or update based on request
        user.setAddressCity(request.getAddressCity()); // Set default city or update based on request
        user.setAddress(""); // Set address if available in request
        user.setMobile(request.getMobile()); // Set mobile number if available in request
        user.setPhone(""); // Set phone number if available in request
        user.setAccountLocked(false); // By default, account is not locked
        user.setEnabled(false); // By default, account is disabled
        user.setShowEmail(false);
        user.setShowMobile(false);
        user.setShowPhone(false);
        user.setShowBadge(true);
        // Assign appropriate roles to the user
        // Example: Assuming 'UserRole' is an enum representing roles like ROLE_USER, ROLE_ADMIN, etc.
        user.setRoles(Collections.singletonList(new Role("USER"))); // Set default role or update based on your logic

        return user;
    }

}
