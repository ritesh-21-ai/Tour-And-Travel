package com.example.TravelAndTour.MainFolder;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {
	 
	@Value("${admin.number}")
	private String adminPhoneNumber;
	private final TravelService travelService;

    @GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
	}

	@GetMapping("/checking")
	public ResponseEntity<Boolean> CheckAdminAccess(@RequestParam String PhoneNumber){ 
		if(PhoneNumber.equals(adminPhoneNumber)) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
	}
	
	@GetMapping("/allPlans")
	public ResponseEntity<List<PlanWithPhotosDTO>> getAllPlans() {
		return ResponseEntity.ok(travelService.getAllPlans());
	}

	@PostMapping("/newplan")
	public ResponseEntity<Tlayout> createPlan(@RequestBody Tlayout entity) {
       	 Tlayout savedPlan = travelService.savePlan(entity);
		return ResponseEntity.ok(savedPlan);
	}

	@DeleteMapping("/deletePlan/{id}")
	public ResponseEntity<Boolean> deletePlan(@PathVariable Long id) {
		boolean deleted = travelService.deletePlan(id);
		if (deleted) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);
		}
	}

    @PutMapping("updatePlan/{id}")
    public ResponseEntity<Tlayout> putPlan(@PathVariable Long id, @RequestBody Tlayout entity) {
                
        return ResponseEntity.ok(travelService.updatePlan(id, entity));
    }


    
}
