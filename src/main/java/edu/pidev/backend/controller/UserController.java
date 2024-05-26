package edu.pidev.backend.controller;

import edu.pidev.backend.common.PageResponse;
import edu.pidev.backend.common.enumuration.TunisianCity;
import edu.pidev.backend.common.enumuration.UserBadge;
import edu.pidev.backend.common.enumuration.UserGender;
import edu.pidev.backend.common.user.UpdateProfileRequest;
import edu.pidev.backend.common.user.UserFilterCriteria;
import edu.pidev.backend.common.user.UserMapper;
import edu.pidev.backend.common.user.UserDTO;
import edu.pidev.backend.entity.User;
import edu.pidev.backend.exception.ForbiddenException;
import edu.pidev.backend.exception.OperationNotPermittedException;
import edu.pidev.backend.security.AccessService;
import edu.pidev.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "User Management", description = "Web Services for User management")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AccessService accessService;

    @Operation(
            operationId = "load-users",
            summary = "Get all users",
            description = "Retrieve a paginated list of users based on filter criteria.")
    @GetMapping("/load")
    public ResponseEntity<PageResponse<UserDTO>> getAll(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "lastModifiedDate") String sort,
                                                        @RequestParam(defaultValue = "DESC") Sort.Direction order,
                                                        @RequestParam(required = false) String search,
                                                        @RequestParam(required = false) UserBadge badge,
                                                        @RequestParam(required = false)  TunisianCity city,
                                                        @RequestParam(required = false)  UserGender gender,
                                                        @RequestParam(defaultValue = "false") boolean locked,
                                                        @RequestParam(defaultValue = "true") boolean enabled) {

        UserFilterCriteria filter = UserFilterCriteria.builder()
                .search(search)
                .badge(badge)
                .city(city)
                .gender(gender)
                .accountLocked(locked)
                .enabled(enabled)
                .build();

        PageResponse<UserDTO> response = userService.getAll(page, size, sort, order, filter);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            operationId = "load-user",
            summary = "Get user by ID",
            description = "Retrieve a user by ID.")
    @GetMapping("/load/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long userId) {
        var user = userService.get(userId);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserDTO(user));
    }

    @Operation(
            operationId = "updateProfile",
            summary = "Update profile",
            description = "Update personal profile details information.")
    @PutMapping("/update-profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateProfileRequest profileRequest) {
        User current = accessService.getCurrentUser();

        if (!Objects.equals(current.getId(), profileRequest.getId())) {
            throw new OperationNotPermittedException("You cannot update others user's information");
        }

        UserMapper.toUser(profileRequest, current);
        UserDTO result = UserMapper.toUserDTO(userService.update(current));

        return ResponseEntity.ok(result);
    }


    @Operation(
            operationId = "lock",
            summary = "Lock or unlock user",
            description = "Lock or unlock a user account by their ID (Admin-only).")
    @PutMapping("/lock")
    public ResponseEntity<?> handleLock(@RequestParam("user_id") Long userId, Authentication authentication) {
        // Check if user is authenticated and has admin role
        if (!accessService.isAdmin()) {
            throw new ForbiddenException("Only Admin can lock/unlock users");
        }
        userService.handleLock(userId);

        return ResponseEntity.noContent().build();
    }


    @Operation(
            operationId = "delete",
            summary = "Delete user",
            description = "Delete a user account by their ID (Admin-only).")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("user_id") Long userId) {
        // Check if user is authenticated and has admin role
        if (!accessService.isAdmin()) {
            throw new ForbiddenException("Only Admin can delete users");
        }

        // Archive (delete or mark as inactive) the user
        userService.handleEnable(userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "changeEmail",
            summary = "Change email",
            description = "Change user email.")
    @PutMapping("/change-email")
    public ResponseEntity<UserDTO> changeEmail(@RequestParam String email, @RequestParam String newEmail) {
        var user = accessService.getCurrentUser();

        if (!Objects.equals(user.getEmail(), email)) {
            throw new ForbiddenException("You do not have access to change this email");
        }

        user = userService.changeEmail(email, newEmail);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));

    }

    @Operation(
            operationId = "updatePassword",
            summary = "Update password",
            description = "Change user password.")
    @PutMapping("/change-password")
    public ResponseEntity<UserDTO> updatePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        var user = userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @Operation(
            operationId = "userExists",
            summary = "User exists",
            description = "User exists by email or not.")
    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> userExists(@RequestParam String email) {
        return ResponseEntity.ok(userService.userExists(email));
    }

    @Operation(
            operationId = "disableAccount",
            summary = "Disable Account",
            description = "User self disable account.")
    @DeleteMapping("/disable")
    public ResponseEntity<?> disableAccount() {
        var user = accessService.getCurrentUser();

        userService.disableAccount(user);
        return ResponseEntity.noContent().build();
    }
}
