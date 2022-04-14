package com.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long num;

    private String floor;

    private Long size;

    private Boolean isAvail;

    private String link;

    private String address;

    private Double lat;

    private Double lon;
    
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}
