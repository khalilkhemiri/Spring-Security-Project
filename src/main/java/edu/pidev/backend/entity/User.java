package edu.pidev.backend.entity;

import edu.pidev.backend.common.Utils;
import edu.pidev.backend.common.enumuration.Language;
import edu.pidev.backend.common.enumuration.TunisianCity;
import edu.pidev.backend.common.enumuration.UserBadge;
import edu.pidev.backend.common.enumuration.UserGender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = true)
    String lastName;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    UserGender gender;

    @Column(name = "birth_date")
    LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_city", nullable = false)
    TunisianCity addressCity;

    @Column(name = "address")
    String address;

    @Column(name = "mobile")
    String mobile;

    @Column(name = "phone")
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge")
    UserBadge badge;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    Language lang;

    @Column(name = "show_email")
    boolean showEmail;

    @Column(name = "show_mobile")
    boolean showMobile;

    @Column(name = "show_phone")
    boolean showPhone;

    @Column(name = "show_badge")
    boolean showBadge;

    Integer profileCompilation;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "role", nullable = false)
//    UserRole role;

    @Column(name = "locked")
    boolean accountLocked;

    @Column(name = "enabled")
    boolean enabled;

    @ManyToMany(fetch = EAGER)
    List<Role> roles;

//    @OneToMany(mappedBy = "owner")
//    private List<Property> properties;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    LocalDateTime lastModifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getName() {
        return email;
    }

    public String getFullName() {
        this.fullName = Utils.capitalizeWords(firstName) + " " + lastName.toUpperCase();
        return this.fullName;
    }

    public int getProfileCompilation() {
        if (profileCompilation == null) {
            return Utils.calculateProfileCompilation(this);
        }
        return profileCompilation;
    }

    public void generateProfileCompilation() {
        this.profileCompilation = Utils.calculateProfileCompilation(this);
    }
}
