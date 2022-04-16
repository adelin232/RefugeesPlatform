package com.project.repositories;

import com.project.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Procedure("FIND_RENTAL_BY_USERID")
    Rental findRentalByUserId(Long userId);
}
