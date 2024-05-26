package edu.pidev.backend.common.user;

import edu.pidev.backend.common.enumuration.Language;
import edu.pidev.backend.common.enumuration.TunisianCity;
import edu.pidev.backend.common.enumuration.UserGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UpdateProfileRequest {

    @NotNull(message = "Id is mandatory")
    @Schema(description = "User ID", required = true, implementation = Long.class)
    long id;

    @NotEmpty(message = "First Name is mandatory")
    @NotNull(message = "First Name is mandatory")
    @Schema(description = "First name of the user", example = "John")
    String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    String lastName;

    @NotEmpty(message = "Gender is mandatory")
    @NotNull(message = "Gender is mandatory")
    @Schema(description = "Gender of the user", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    UserGender gender;

    @NotEmpty(message = "Birth Date is mandatory")
    @NotNull(message = "Birth Date is mandatory")
    @Schema(description = "Birth date of the user (yyyy-MM-dd)", example = "1990-01-01")
    LocalDate birthDate;

    @NotEmpty(message = "City is mandatory")
    @NotNull(message = "City is mandatory")
    @Schema(description = "City of address", example = "Tunis")
    TunisianCity addressCity;

    @NotEmpty(message = "Language is mandatory")
    @NotNull(message = "Language is mandatory")
    @Schema(description = "Preferred language", example = "EN", allowableValues = {"EN", "FR"})
    Language lang;

    @Schema(description = "Address of the user", example = "123 Main St, City")
    String address;

    @NotEmpty(message = "Mobile phone is mandatory")
    @NotNull(message = "Mobile phone is mandatory")
    @Schema(description = "Mobile phone number of the user", example = "98000000")
    String mobile;

    @Schema(description = "Landline phone number of the user", example = "71000000")
    String phone;

    boolean showEmail;
    boolean showMobile;
    boolean showPhone;
    boolean showBadge;

}
