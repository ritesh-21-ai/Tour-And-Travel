package com.example.TravelAndTour.MainFolder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPhotoRepo extends JpaRepository<TravelPhoto, Long> {
    
}
