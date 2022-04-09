package com.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String email;

    private String phone;

    private String first_name;

    private String last_name;

    private String full_name;

//    @OneToMany(mappedBy = "user")
//    private List<Room> rooms;
}
