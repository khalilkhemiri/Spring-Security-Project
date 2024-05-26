package edu.pidev.backend.controller;

import edu.pidev.backend.service.passwordreset.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Password Reset", description = "Web Services for Password Reset")
@RestController
@RequiredArgsConstructor
@RequestMapping("/password-reset")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @Operation(
            operationId = "resetPassword",
            summary = "Request reset password",
            description = "Send a password reset request to email.")
    @PostMapping("/request")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestParam String email) throws MessagingException {
        passwordResetService.forgotPassword(email);
        return  ResponseEntity.ok( new ResetPasswordResponse("A request has been sent to email '" + email + "'"));
    }


    @Operation(
            operationId = "changePassword",
            summary = "Change password",
            description = "Send a new password to change.")
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(ResetPasswordRequest request) throws MessagingException {
        passwordResetService.updatePassword(request.password, request.token);
        return ResponseEntity.accepted().build();
    }

    public record ResetPasswordRequest(String token, String password) {
    }



    public record ResetPasswordResponse(String message) {}
}
