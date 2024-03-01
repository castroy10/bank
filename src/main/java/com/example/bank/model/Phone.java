package com.example.bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "phone",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "phone_number_key",
                        columnNames = "number"
                )
        })
@Data
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private Appuser appuser;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Phone{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

