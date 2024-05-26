package edu.pidev.backend.service.passwordreset;

import jakarta.mail.MessagingException;

public interface IPasswordResetService {

    void forgotPassword(String email) throws MessagingException;

    void updatePassword(String password, String token) throws MessagingException;
    
}
