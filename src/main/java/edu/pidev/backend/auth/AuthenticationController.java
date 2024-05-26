package edu.pidev.backend.auth;

import edu.pidev.backend.common.user.UserMapper;
import edu.pidev.backend.common.user.UserDTO;
import edu.pidev.backend.entity.User;
import edu.pidev.backend.security.JwtService;
import edu.pidev.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Web Services for authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/verify_token")
    public ResponseEntity<UserDTO> getUserByToken(@RequestParam String token) {
        User user = service.getUserByToken(token);
        // Return the response received from the external API
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserMapper.toUserDTO(user));
    }

}
