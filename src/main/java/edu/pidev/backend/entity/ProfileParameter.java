package edu.pidev.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile_params")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String language;

    String theme;
    

    String value;

    @ManyToOne
    User user;
}
