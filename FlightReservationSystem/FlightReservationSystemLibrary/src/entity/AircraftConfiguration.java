/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author yylow
 */
@Entity
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigId;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @NotBlank
    @Size(max = 64, message = "Aircraft Configuration name has maximum 64 characters!")
    private String aircraftConfigName;
    @Column(nullable = false)
    @NotNull
    @Min(value = 1, message = "Minimum number of Cabin Classes is 1!")
    @Max(value = 4, message = "Maximum number of Cabin Classes is 4!")
    private Integer numberOfCabinClass;
    @Column(nullable = false)
    @NotNull 
    // No Positive bean validator here as max capacity is calculated from numberOfCabinClass
    private Integer maximumSeatCapacity;
    
    @OneToMany(mappedBy = "aircraftConfig")
    private List<CabinClassConfiguration> cabinClassConfigs;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftType aircraftType;
    
    
    public AircraftConfiguration() {
        this.cabinClassConfigs = new ArrayList<>();
    }

    public AircraftConfiguration(String aircraftConfigName, Integer numberOfCabinClass, Integer maximumSeatCapacity) {
        this();
        this.aircraftConfigName = aircraftConfigName;
        this.numberOfCabinClass = numberOfCabinClass;
        this.maximumSeatCapacity = maximumSeatCapacity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigId != null ? aircraftConfigId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigId fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigId == null && other.aircraftConfigId != null) || (this.aircraftConfigId != null && !this.aircraftConfigId.equals(other.aircraftConfigId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfigurationEntity[ id=" + aircraftConfigId + " ]";
    }

    public Long getAircraftConfigId() { 
        return aircraftConfigId;
    }

    public void setAircraftConfigId(Long aircraftConfigId) {
        this.aircraftConfigId = aircraftConfigId;
    }

    public String getAircraftConfigName() {
        return aircraftConfigName;
    }

    public void setAircraftConfigName(String aircraftConfigName) {
        this.aircraftConfigName = aircraftConfigName;
    }

    public Integer getNumberOfCabinClass() {
        return numberOfCabinClass;
    }

    public void setNumberOfCabinClass(Integer numberOfCabinClass) {
        this.numberOfCabinClass = numberOfCabinClass;
    }

    public Integer getMaximumSeatCapacity() {
        return maximumSeatCapacity;
    }

    public void setMaximumSeatCapacity(Integer maximumSeatCapacity) {
        this.maximumSeatCapacity = maximumSeatCapacity;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<CabinClassConfiguration> getCabinClassConfigs() {
        return cabinClassConfigs;
    }

    public void setCabinClassConfigs(List<CabinClassConfiguration> cabinClassConfigs) {
        this.cabinClassConfigs = cabinClassConfigs;
    }
    
}
