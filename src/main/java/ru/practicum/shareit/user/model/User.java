package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name")
    @NotBlank(message = "Name у User не должен быть пустым")
    private String name;
    @Column(name = "email", unique = true)
    @Email(message = "Email у User должен быть корректным")
    @NotNull(message = "Email у User должен существовать")
    private String email;
}