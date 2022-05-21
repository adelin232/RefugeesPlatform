package com.project.db;

import com.project.BaseIT;
import com.project.models.Rental;
import com.project.repositories.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RentalTestIT extends BaseIT {

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    void insertRentals() {
        rentalRepository.deleteAll();

        for (long i = 1; i <= 5; ++i) {
            Rental rental = new Rental(i, i, i, "05/06/2022", "06/07/2022");
            rentalRepository.save(rental);
        }
    }

    @Test
    void itShouldFindAll() {
        //when
        var rentalsList = rentalRepository.findAll();
        //then
        assertEquals(rentalsList.size(), 5);
    }
}
