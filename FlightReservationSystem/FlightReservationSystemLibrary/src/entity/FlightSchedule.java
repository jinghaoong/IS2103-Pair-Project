/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private Date departureDateTime;
    @Column(nullable = false, length = 2)
    private Integer estimatedFlightDurationHour;
    @Column(nullable = false, length = 2)
    private Integer estimatedFlightDurationMinute;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date arrivalDateTime;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
    
    @OneToMany
    private List<SeatInventory> seatInventories;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightReservation> flightReservations;

    public FlightSchedule() {
        this.seatInventories = new ArrayList<>();
        this.flightReservations = new ArrayList<>();
    }

    public FlightSchedule(Date departureDateTime, Integer estimatedFlightDurationHour, Integer estimatedFlightDurationMinute) {
        this();
        this.departureDateTime = departureDateTime;
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
    
    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
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

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<SeatInventory> getSeatInventories() {
        return seatInventories;
    }

    public void setSeatInventories(List<SeatInventory> seatInventories) {
        this.seatInventories = seatInventories;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
    
    public void computeAndSetArrivalDateTime() {
        Calendar adt = Calendar.getInstance();
        adt.setTime(this.departureDateTime);
        
        Integer timeZoneDifference = this.flight.getFlightRoute().getOriginAirport().getTimeZone() - this.flight.getFlightRoute().getDestinationAirport().getTimeZone();
        Integer adjustedDurationHour = this.estimatedFlightDurationHour + timeZoneDifference;
        
        adt.add(Calendar.HOUR, adjustedDurationHour);
        adt.add(Calendar.MINUTE, this.estimatedFlightDurationMinute);
        
        setArrivalDateTime(adt.getTime());
    }
}
