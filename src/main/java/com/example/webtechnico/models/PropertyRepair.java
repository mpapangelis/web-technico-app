package com.example.webtechnico.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
//import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyRepair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    //@Column(nullable = false)
    //@Enumerated(EnumType.STRING)
    private TypeOfRepairEnum typeOfRepair;

    private String shortDescription;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date submissionDate;

    //@Column(nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date proposedStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date proposedEndDate;

    private int proposedCost;

    private boolean ownerAcceptance;

    //@Column(nullable = false)
    //@Enumerated(EnumType.STRING)
    private StatusOfRepairEnum status;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date actualStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date actualEndDate;
    
    private Boolean isActive;

//    @Override
//    public String toString() {
//        return "PropertyRepair{" + "repairId=" + repairId + ", propertyOwner=" + propertyOwner.getId() + ", property=" + property.getId() + ", typeOfRepair=" + typeOfRepair + ", shortDescription=" + shortDescription + ", submissionDate=" + submissionDate + ", description=" + description + ", proposedStartDate=" + proposedStartDate + ", proposedEndDate=" + proposedEndDate + ", proposedCost=" + proposedCost + ", ownerAcceptance=" + ownerAcceptance + ", status=" + status + ", actualStartDate=" + actualStartDate + ", actualEndDate=" + actualEndDate + ", isActive=" + isActive +'}';
//    }
}
