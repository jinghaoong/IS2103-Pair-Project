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
import javax.persistence.CascadeType;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min = 3, max = 16, message = "Flight Number has to be between 3 to 16 characters!")
    private String flightNumber;
    @Enumerated(EnumType.STRING)
    private FlightScheduleType flightScheduleType;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    @Column(nullable = true)
    @Positive
    private Integer nDay; // for recurrent every n day
    @Column(nullable = true)
    @PositiveOrZero
    @Min(value = 0, message = "Day of week is 0-based, minimum value is 0!")
    @Max(value = 6, message = "Day of week is 0-based, maximum value is 6!")
    private Integer dayOfWeek; // for recurrent weekly (Sun: 0, .... Sat: 6)
    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date endDate;
    @Column(nullable = false)
    @NotNull
    private Boolean enabled;
    
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = CascadeType.ALL)
    private List<FlightSchedule> flightSchedules;
    
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = CascadeType.ALL)
    private List<Fare> fares;
    
    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    private FlightSchedulePlan returnFlightSchedulePlan;
    
    public FlightSchedulePlan() {
        this.flightSchedules = new ArrayList<>();
        this.fares = new ArrayList<>();
    }
    
    public FlightSchedulePlan(String flightNumber, FlightScheduleType flightScheduleType) {
        this();
        this.enabled = true;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate() {
        this.startDate = this.flightSchedules.get(0).getDepartureDateTime();
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }
    
}
