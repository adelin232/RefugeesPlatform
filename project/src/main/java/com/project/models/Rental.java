package com.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long roomId;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
}
