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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
    private String aircraftConfigName;
    @Column(nullable = false)
    private Integer numberOfCabinClass;
    @Column(nullable = false)
    private Integer maximumSeatCapacity;
    
    @ManyToOne(optional = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfig")
    private List<CabinClassConfiguration> cabinClassConfigs;
    
    @OneToOne(optional = true)
    private Flight flight;

    public AircraftConfiguration() {
        this.cabinClassConfigs = new ArrayList<>();
    }

    public AircraftConfiguration(String aircraftConfigName, Integer numberOfCabinClass, Integer maximumSeatCapacity, AircraftType aircraftType, List<CabinClassConfiguration> cabinClassConfigs) {
        this.aircraftConfigName = aircraftConfigName;
        this.numberOfCabinClass = numberOfCabinClass;
        this.maximumSeatCapacity = maximumSeatCapacity;
        this.aircraftType = aircraftType;
        this.cabinClassConfigs = cabinClassConfigs;
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

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
}
