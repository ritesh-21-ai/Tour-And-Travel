package com.example.TravelAndTour.MainFolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*")
public class PhotoController {
    
    private final TravelService travelService;

    // CREATE: Add images to an existing plan (Max 2)
    @PostMapping(value = "/add/{planId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhotos(
            @PathVariable Long planId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            String message = travelService.addPhotosToPlan(planId, files);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ: Get a specific photo by ID
    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long photoId) {
        byte[] image = travelService.getPhotoById(photoId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    // DELETE: Remove a photo
    @DeleteMapping("/delete/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long photoId) {
        boolean deleted = travelService.deletePhoto(photoId);
        if (deleted) {
            return ResponseEntity.ok("Photo deleted successfully.");
        }
        return ResponseEntity.status(404).body("Photo not found.");
    }
}