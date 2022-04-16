package com.project.repositories;

import com.project.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
//    @Procedure("FIND_USER_BY_EMAIL")
//    User findUserByEmail(String email);
}
