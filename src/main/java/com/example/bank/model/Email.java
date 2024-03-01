package com.example.bank.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "email",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "email_email_key",
                        columnNames = "email"
                )
        })
@Data
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private Appuser appuser;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Email{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
