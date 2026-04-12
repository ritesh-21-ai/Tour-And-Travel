package com.example.TravelAndTour.MainFolder;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TravelPhoto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Lob
     @JdbcTypeCode(SqlTypes.BINARY) 
     @Column(columnDefinition="BYTEA")
     private byte[] picbyte;

    private String fileType;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private TravelPlan travelPlan;

    public TravelPhoto(byte[] picbyte, String fileType, TravelPlan travelPlan) {
        this.picbyte = picbyte;
        this.fileType = fileType;
        this.travelPlan = travelPlan;
    }
    

}
