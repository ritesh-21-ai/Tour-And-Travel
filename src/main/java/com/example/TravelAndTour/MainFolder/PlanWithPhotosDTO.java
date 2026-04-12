package com.example.TravelAndTour.MainFolder;

import java.util.List;

import com.example.TravelAndTour.MainFolder.TravelService.PhotoDTO;

public record PlanWithPhotosDTO(
    Tlayout planDetails,
    List<PhotoDTO> photos
) {}
