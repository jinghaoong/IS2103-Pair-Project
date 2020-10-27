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
import javax.persistence.OneToMany;

/**
 *
 * @author jinghao
 */
@Entity
public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftTypeId;
    @Column(nullable = false, length = 32)
    private String aircraftTypeName;
    @Column(nullable = false)
    private Integer maximumPassengerCapacity;
    
    @OneToMany(mappedBy = "aircraftType")
    private List<AircraftConfiguration> aircraftConfigurations;

    public AircraftType() {
        this.aircraftConfigurations = new ArrayList<>();
    }

    public AircraftType(String aircraftTypeName, Integer maximumPassengerCapacity) {
        this.aircraftTypeName = aircraftTypeName;
        this.maximumPassengerCapacity = maximumPassengerCapacity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftTypeId != null ? aircraftTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftTypeId fields are not set
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.aircraftTypeId == null && other.aircraftTypeId != null) || (this.aircraftTypeId != null && !this.aircraftTypeId.equals(other.aircraftTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftTypeEntity[ id=" + aircraftTypeId + " ]";
    }

    public Long getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(Long aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public Integer getMaximumPassengerCapacity() {
        return maximumPassengerCapacity;
    }

    public void setMaximumPassengerCapacity(Integer maximumPassengerCapacity) {
        this.maximumPassengerCapacity = maximumPassengerCapacity;
    }

    public List<AircraftConfiguration> getAircraftConfigurations() {
        return aircraftConfigurations;
    }

    public void setAircraftConfigurations(List<AircraftConfiguration> aircraftConfigurations) {
        this.aircraftConfigurations = aircraftConfigurations;
    }
    
}
