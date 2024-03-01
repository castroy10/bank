package com.example.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "appuser",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "appuser_username_key",
                        columnNames = "username"
                )
        })
@Data
public class Appuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "first_balance")
    private BigDecimal firstBalance;

    @Column(name = "account_nonexpired")
    private boolean accountNonExpired;
    @Column(name = "account_nonlocked")
    private boolean accountNonLocked;
    @Column(name = "credentials_nonexpired")
    private boolean credentialsNonExpired;
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @OneToMany(mappedBy = "appuser", cascade = CascadeType.ALL)
    private List<Phone> phones;
    @OneToMany(mappedBy = "appuser", cascade = CascadeType.ALL)
    private List<Email> emails;

    @Override
    public String toString() {
        return new StringJoiner(", ", Appuser.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("middleName='" + middleName + "'")
                .add("lastName='" + lastName + "'")
                .add("birthday=" + birthday + "'")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("accountNonExpired=" + accountNonExpired)
                .add("accountNonLocked=" + accountNonLocked)
                .add("credentialsNonExpired=" + credentialsNonExpired)
                .add("isEnabled=" + isEnabled)
                .toString();
    }
}
