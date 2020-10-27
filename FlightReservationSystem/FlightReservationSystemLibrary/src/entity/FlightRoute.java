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
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
    @Column(nullable = false)
    private Boolean enabled;
    
    @ManyToOne(optional = false)
    private Airport originAirport;
    
    @ManyToOne(optional = false)
    private Airport destinationAirport;
    
    @OneToMany(mappedBy = "flightRoute")
    private List<Flight> flights;
    
    @OneToOne(optional = true)
    private FlightRoute returnFlightRoute;

    public FlightRoute() {
        this.flights = new ArrayList<>();
        this.returnFlightRoute = null;
    }

    public FlightRoute(Boolean enabled, Airport origin, Airport destination) {
        this();
        this.enabled = enabled;
        this.originAirport = origin;
        this.destinationAirport = destination;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightRouteId fields are not set
        if (!(object instanceof FlightRoute)) {
            return false;
        }
        FlightRoute other = (FlightRoute) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRouteEntity[ id=" + flightRouteId + " ]";
    }
    
    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Airport getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(Airport originAirport) {
        this.originAirport = originAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public FlightRoute getReturnFlightRoute() {
        return returnFlightRoute;
    }

    public void setReturnFlightRoute(FlightRoute returnFlightRoute) {
        this.returnFlightRoute = returnFlightRoute;
    }

}
