package com.example.TravelAndTour.MainFolder;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelService {
    private final TravelPlanRepo travelPlanRepo;
    private final TravelPhotoRepo travelPhotoRepo; 
    
    public List<PlanWithPhotosDTO> getAllPlans() {

        List<TravelPlan> travelPlans = travelPlanRepo.findAll();
          return travelPlans.stream().map(plan -> {
            Tlayout details = Tlayout.fromTravelPlan(plan);
            
            // CRITICAL FIX: We are now sending the photo.getId() instead of getPicbyte()
            List<PhotoDTO> photoDTOs = plan.getPhotos().stream()
                .map(photo -> new PhotoDTO(photo.getId(), photo.getFileType()))
                .collect(Collectors.toList());
                
            return new PlanWithPhotosDTO(details, photoDTOs);
        }).collect(Collectors.toList());
    }

    public Tlayout savePlan(Tlayout tlayout) {
        TravelPlan travelPlan = Tlayout.toTravelPlan(tlayout);
        TravelPlan savedPlan = travelPlanRepo.save(travelPlan);
        return Tlayout.fromTravelPlan(savedPlan);
    }

    public boolean deletePlan(Long id) {
    try {
        // 1. Fetch the plan first to manage its relationships
        TravelPlan plan = travelPlanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));

        // 2. Clear the photos list (orphanRemoval will trigger deletion in DB)
        plan.getPhotos().clear();
        
        // 3. Delete the plan itself
        travelPlanRepo.delete(plan);
        
        return true;
    } catch (Exception e) {
        log.error("Error deleting plan with id {}: {}", id, e.getMessage());
        return false;
    }
}
     
   public Tlayout updatePlan(Long id, Tlayout tlayout) {
    TravelPlan existingPlan = travelPlanRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));

    log.info("Updating Plan ID {}: Comparing old data with new input.", id);

    if (tlayout.name() != null && !tlayout.name().equals(existingPlan.getName())) {
        existingPlan.setName(tlayout.name());
    }

    if (tlayout.description() != null && !tlayout.description().equals(existingPlan.getDescription())) {
        existingPlan.setDescription(tlayout.description());
    }

    if (tlayout.message() != null && !tlayout.message().equals(existingPlan.getMessage())) {
        existingPlan.setMessage(tlayout.message());
    }

    if (tlayout.price() > 0 && tlayout.price() != existingPlan.getPrice()) {
        existingPlan.setPrice(tlayout.price());
    }
    TravelPlan updatedPlan = travelPlanRepo.save(existingPlan);
    
    return Tlayout.fromTravelPlan(updatedPlan);
}

  public String addPhotosToPlan(Long planId, List<MultipartFile> files) throws java.io.IOException {
    TravelPlan plan = travelPlanRepo.findById(planId)
            .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));

    // Enforcement of maximum 2 images
    if ((plan.getPhotos().size() + files.size()) > 2) {
        throw new RuntimeException("Limit exceeded: A travel plan can have a maximum of 2 images.");
    }

    for (MultipartFile file : files) {
        TravelPhoto photo = new TravelPhoto(
            file.getBytes(), 
            file.getContentType(), 
            plan
        );
        // Add photo to the plan's collection
        plan.getPhotos().add(photo);
    }

    // Save the plan ONCE (cascade will save all photos)
    travelPlanRepo.save(plan);
    
    return "Successfully uploaded " + files.size() + " photo(s).";
}

public byte[] getPhotoById(Long photoId) {
    TravelPhoto photo = travelPhotoRepo.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));
    return photo.getPicbyte();
}

public boolean deletePhoto(Long photoId) {
    if (travelPhotoRepo.existsById(photoId)) {
        travelPhotoRepo.deleteById(photoId);
        return true;
    }
    return false;
}

public record PhotoDTO(
    Long id,
    byte[] data,
    String fileType
) {
    public PhotoDTO(Long id, String fileType) {
        this(id, null, fileType);
    }
}

}
