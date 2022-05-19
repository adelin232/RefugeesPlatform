package com.project;

import com.project.models.Rental;
import com.project.repositories.RentalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class RentalTests {

    @Autowired
    RentalRepository rentalRepository;

    @Test
    @DisplayName("Insert rental should work")
    void insertUserTest() {
        Rental rental = new Rental(1L, 1L, 1L, "04/05/2022", "08/09/2022");

        rentalRepository.save(rental);
        assertThat(rentalRepository.findById(rental.getId())).hasValue(rental);
    }
}
