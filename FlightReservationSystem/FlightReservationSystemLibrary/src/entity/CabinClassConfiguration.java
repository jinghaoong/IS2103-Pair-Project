/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import util.enumeration.CabinClass;

/**
 *
 * @author jinghao
 */
@Entity
public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassConfigId;
    @Enumerated(EnumType.STRING)
    private CabinClass cabinClass;
    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    @Max(value = 2, message = "Maximum number of aisles is 2")
    private Integer numberOfAisles;
    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer numberOfRows;
    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer numberOfSeatsAbreast;
    @Column(nullable = false)
    @NotNull
    private Integer[] seatingConfig; // e.g. 3-4-3, 3-3
    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer maximumCapacity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    AircraftConfiguration aircraftConfig;
    
    
    public CabinClassConfiguration() {
    }

    public CabinClassConfiguration(CabinClass cabinClass, Integer numberOfAisles, Integer numberOfRows, Integer numberOfSeatsAbreast, Integer[] seatingConfig) {
        this.cabinClass = cabinClass;
        this.numberOfAisles = numberOfAisles;
        this.numberOfRows = numberOfRows;
        this.numberOfSeatsAbreast = numberOfSeatsAbreast;
        this.seatingConfig = seatingConfig;
        this.maximumCapacity = numberOfRows * numberOfSeatsAbreast;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassConfigId != null ? cabinClassConfigId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassConfigId fields are not set
        if (!(object instanceof CabinClassConfiguration)) {
            return false;
        }
        CabinClassConfiguration other = (CabinClassConfiguration) object;
        if ((this.cabinClassConfigId == null && other.cabinClassConfigId != null) || (this.cabinClassConfigId != null && !this.cabinClassConfigId.equals(other.cabinClassConfigId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClassConfiguration[ id=" + cabinClassConfigId + " ]";
    }
    
    public Long getCabinClassConfigId() {
        return cabinClassConfigId;
    }

    public void setCabinClassConfigId(Long cabinClassConfigId) {
        this.cabinClassConfigId = cabinClassConfigId;
    }

    public CabinClass getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    public Integer getNumberOfAisles() {
        return numberOfAisles;
    }

    public void setNumberOfAisles(Integer numberOfAisles) {
        this.numberOfAisles = numberOfAisles;
    }

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public Integer getNumberOfSeatsAbreast() {
        return numberOfSeatsAbreast;
    }

    public void setNumberOfSeatsAbreast(Integer numberOfSeatsAbreast) {
        this.numberOfSeatsAbreast = numberOfSeatsAbreast;
    }

    public Integer[] getSeatingConfig() {
        return seatingConfig;
    }

    public void setSeatingConfig(Integer[] seatingConfig) {
        this.seatingConfig = seatingConfig;
    }

    public AircraftConfiguration getAircraftConfig() {
        return aircraftConfig;
    }

    public void setAircraftConfig(AircraftConfiguration aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }

    public Integer getNumSeats() {
        return this.numberOfRows * this.numberOfSeatsAbreast;
    }

    public Integer getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(Integer maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

}
