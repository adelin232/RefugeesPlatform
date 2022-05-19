package com.project.services;

import com.project.models.Rental;
import com.project.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public void createRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public Rental getRental(Long id) {
        return rentalRepository.getById(id);
    }

    public Rental findRentalByUserId(Long userId) {
        return rentalRepository.findRentalByUserId(userId);
    }

    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }
}
