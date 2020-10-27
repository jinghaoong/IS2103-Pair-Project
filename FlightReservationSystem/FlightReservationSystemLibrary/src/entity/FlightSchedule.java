/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jinghao
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateTime;
    @Column(nullable = false)
    private Integer estimatedFlightDurationHour;
    @Column(nullable = false)
    private Integer estimatedFlightDurationMinute;

    public FlightSchedule() {
    }

    public FlightSchedule(Date dateTime, Integer estimatedFlightDurationHour, Integer estimatedFlightDurationMinute) {
        this.dateTime = dateTime;
        this.estimatedFlightDurationHour = estimatedFlightDurationHour;
        this.estimatedFlightDurationMinute = estimatedFlightDurationMinute;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleId + " ]";
    }
    
    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getEstimatedFlightDurationHour() {
        return estimatedFlightDurationHour;
    }

    public void setEstimatedFlightDurationHour(Integer estimatedFlightDurationHour) {
        this.estimatedFlightDurationHour = estimatedFlightDurationHour;
    }

    public Integer getEstimatedFlightDurationMinute() {
        return estimatedFlightDurationMinute;
    }

    public void setEstimatedFlightDurationMinute(Integer estimatedFlightDurationMinute) {
        this.estimatedFlightDurationMinute = estimatedFlightDurationMinute;
    }
    
}
