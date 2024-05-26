package edu.pidev.backend.service.user;

import edu.pidev.backend.common.PageResponse;
import edu.pidev.backend.common.Pagination;
import edu.pidev.backend.common.Utils;
import edu.pidev.backend.common.user.*;
import edu.pidev.backend.entity.User;

import edu.pidev.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User add(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("Username already exists");
        });

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Email already exists");
        });

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(false);
        user.setCreatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }


    @Override
    public User update(User user) {
        // Retrieve the current user from the database using the user's ID
        Optional<User> existingUserOptional = userRepository.findById(user.getId());
        if (existingUserOptional.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User existingUser = existingUserOptional.get();


        // Update username if provided and different
        String newUsername = user.getUsername();
        if (newUsername != null && !newUsername.equals(existingUser.getUsername())) {
            userRepository.findByUsername(newUsername).ifPresent(u -> {
                throw new IllegalStateException("Username already exists");
            });
            existingUser.setUsername(newUsername);
        }

        // Update email if provided and different
        String newEmail = user.getEmail();
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            userRepository.findByEmail(newEmail).ifPresent(u -> {
                throw new IllegalStateException("Email already exists");
            });
            existingUser.setEmail(newEmail);
        }

        // Update password if provided and different
        String newPassword = user.getPassword();
        if (newPassword != null && !newPassword.equals(existingUser.getPassword())) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }


        user.generateProfileCompilation();
        user.setLastModifiedDate(LocalDateTime.now());

        // Generate formatted full name
        Utils.formatUserName(user);

        // Save the updated user
        return userRepository.save(user);
    }

    @Override
    public PageResponse<UserDTO> getAll(int page, int size, String sort, Sort.Direction order, UserFilterCriteria filter) {

        // Create a PageRequest object to handle pagination and sorting
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(order, sort));

        // Retrieve the appropriate page of counterparties based on the provided search and filter parameters
        UserSpecification specification = new UserSpecification(filter);

        Page<User> userPage = userRepository.findAll(specification, pageable);

        // Extract a list of User objects from the page
        List<User> userList = userPage.getContent();

        List<UserDTO> userDTOList = userList.stream()
                .map(UserMapper::toUserDTO)
                .toList();

        // Retrieve metadata about the retrieved page of users
        int number = userPage.getNumber() + 1;
        size = userPage.getSize();
        long totalElements = userPage.getTotalElements();
        int totalPages = userPage.getTotalPages();
        boolean isFirst = userPage.isFirst();
        boolean isLast = userPage.isLast();


        // Create a Pagination object to represent the retrieved metadata
        Pagination pagination = new Pagination(number, size, totalElements, totalPages, isFirst, isLast);

        // Return a ResponseEntity object with the QueryResponse object
        return new PageResponse<>(userDTOList, pagination);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id '" + id + "' not found"));
    }

    @Override
    public User get(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email '" + email + "' not found"));
    }

    @Override
    @Transactional
    public void handleEnable(Long id) {
        User user = get(id);
        boolean enabled = user.isEnabled();
        user.setEnabled(!enabled);
    }

    @Override
    @Transactional
    public void handleLock(Long id) {
        User user = get(id);
        var locked = user.isAccountLocked();
        user.setAccountLocked(!locked);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User changeEmail(String oldEmail, String newEmail) {
        if (emailExists(newEmail)) {
            throw new IllegalStateException("Email '" + newEmail + "' exists!");
        }
        var user = get(oldEmail);

        user.setEmail(newEmail);
        user.setUsername(newEmail);

        return userRepository.save(user);
    }

    @Override
    public User changePassword(Long userId, String oldPassword, String newPassword) {
        var user = get(userId);

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        } else {
            throw new IllegalStateException("Password is incorrect!");
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(username);
    }

    @Override
    public void disableAccount(User user) {
        user.setEnabled(false);
        userRepository.save(user);
    }


}
