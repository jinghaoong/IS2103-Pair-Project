/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.FlightScheduleType;

/**
 *
 * @author jinghao
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    @Column(nullable = false, length = 16)
    private String flightNumber;
    @Enumerated(EnumType.STRING)
    private FlightScheduleType flightScheduleType;
    @Column(nullable = true)
    private Integer nDay; // for recurrent every n day
    @Column(nullable = true)
    private Integer dayOfWeek; // for recurrent weekly (Sun: 0, .... Sat: 6)
    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date endDate;
    
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightSchedule> flightSchedules;
    
    @OneToOne(optional = true)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan returnFlightSchedulePlan;
    
    public FlightSchedulePlan() {
        this.flightSchedules = new ArrayList<>();
    }

    public FlightSchedulePlan(String flightNumber, FlightScheduleType flightScheduleType) {
        this();
        this.flightNumber = flightNumber;
        this.flightScheduleType = flightScheduleType;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanId + " ]";
    }
    
    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public FlightScheduleType getFlightScheduleType() {
        return flightScheduleType;
    }

    public void setFlightScheduleType(FlightScheduleType flightScheduleType) {
        this.flightScheduleType = flightScheduleType;
    }

    public Integer getnDay() {
        return nDay;
    }

    public void setnDay(Integer nDay) {
        this.nDay = nDay;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public FlightSchedulePlan getReturnFlightSchedulePlan() {
        return returnFlightSchedulePlan;
    }

    public void setReturnFlightSchedulePlan(FlightSchedulePlan returnFlightSchedulePlan) {
        this.returnFlightSchedulePlan = returnFlightSchedulePlan;
    }

    public void addFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedules.add(flightSchedule);
    }
    
}
