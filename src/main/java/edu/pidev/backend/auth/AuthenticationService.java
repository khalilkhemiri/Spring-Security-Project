package edu.pidev.backend.auth;

import edu.pidev.backend.common.Utils;
import edu.pidev.backend.common.enumuration.TokenType;
import edu.pidev.backend.common.enumuration.UserBadge;
import edu.pidev.backend.common.user.UserMapper;
import edu.pidev.backend.email.EmailService;
import edu.pidev.backend.email.EmailTemplateName;
import edu.pidev.backend.exception.ExpiredTokenException;
import edu.pidev.backend.exception.InvalidTokenException;
import edu.pidev.backend.repository.RoleRepository;
import edu.pidev.backend.security.JwtService;
import edu.pidev.backend.entity.Token;
import edu.pidev.backend.repository.TokenRepository;
import edu.pidev.backend.entity.User;
import edu.pidev.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${application.default-active}")
    private static boolean ACTIVE;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        var user = UserMapper.mapRegistrationRequestToUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singletonList(userRole));
        user.setBadge(UserBadge.NEW_MEMBER);
        user.setEnabled(ACTIVE);

        Utils.formatUserName(user);

        userRepository.save(user);
        sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .refreshToken(jwtToken)
                .expiresIn(LocalDate.now())
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        LocalDateTime tokenExpiresAt;
        LocalDateTime now = LocalDateTime.now();

        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (!savedToken.getType().equals(TokenType.ACTIVATE_ACCOUNT)) {
            throw new InvalidTokenException("Invalid token type");
        }

        tokenExpiresAt = savedToken.getExpiresAt();

        if (savedToken.getValidatedAt() == null) {
            if (tokenExpiresAt.isBefore(now)) {
                sendValidationEmail(savedToken.getUser());
                throw new ExpiredTokenException("Activation token has expired. A new token has been send to the same email address");
            }

            var user = userRepository.findById(savedToken.getUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            user.setEnabled(true);
            userRepository.save(user);

            savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepository.save(savedToken);
        }
        else {
            throw  new InvalidTokenException("Token Already Used");
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = Utils.generateTokenCode(6, TokenType.ACTIVATE_ACCOUNT);
        var token = Token.builder()
                .token(generatedToken)
                .type(TokenType.ACTIVATE_ACCOUNT)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    public User getUserByToken(String jwt) {
        // Extract the user email from the token
        String userEmail = jwtService.extractUsername(jwt);

        // Retrieve the user from the repository using the email
        User user = userRepository.findByUsername(userEmail).orElse(null);

        // Return null if the user doesn't exist or the token is not valid for the user
        if (user == null || !jwtService.isTokenValid(jwt, user)) {
            return null;
        }

        return user;

    }
}
