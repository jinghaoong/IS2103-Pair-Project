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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author yylow
 */
@Entity
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull 
    @NotBlank
    @Size(max = 64, message = "Airport name has a maximum of 64 characters!")
    private String airportName;
    @Column(nullable = false, length = 3, unique = true)
    @NotNull
    @Size(min = 3, max = 3, message = "Airport IATA codes are exactly 3 characters long.")
    private String airportCode;
    @Column(nullable = false, length = 32)
    @NotNull
    @NotBlank
    @Size(max = 32, message = "City name has a maximmum of 32 characters!")
    private String city;
    @Column(nullable = false, length = 32)
    @NotNull
    @NotBlank
    @Size(max = 32, message = "State/Province name has a maximum of 32 characters!")
    private String stateProvince;
    @Column(nullable = false, length = 32)
    @NotNull
    @NotBlank
    @Size(max = 32, message = "Country name has a maximum of 32 characters!")
    private String country;
    @Column(nullable = false)
    @NotNull()
    private Integer timeZone;
    
    @OneToMany(mappedBy = "originAirport")
    private List<FlightRoute> originFlightRoutes;
    
    @OneToMany(mappedBy = "destinationAirport")
    private List<FlightRoute> destinationFlightRoutes;
    
    public Airport() {
        this.originFlightRoutes = new ArrayList<>();
        this.destinationFlightRoutes = new ArrayList<>();
    }

    public Airport(String airportName, String airportCode, String city, String stateprovince, String country, Integer timeZone) {
        this();
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.city = city;
        this.stateProvince = stateprovince;
        this.country = country;
        this.timeZone = timeZone;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportId != null ? airportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportId fields are not set
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.airportId == null && other.airportId != null) || (this.airportId != null && !this.airportId.equals(other.airportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AirportEntity[ id=" + airportId + " ]";
    }

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }
    
    public List<FlightRoute> getOriginFlightRoutes() {
        return originFlightRoutes;
    }

    public void setOriginFlightRoutes(List<FlightRoute> originFlightRoutes) {
        this.originFlightRoutes = originFlightRoutes;
    }

    public List<FlightRoute> getDestinationFlightRoutes() {
        return destinationFlightRoutes;
    }

    public void setDestinationFlightRoutes(List<FlightRoute> destinationFlightRoutes) {
        this.destinationFlightRoutes = destinationFlightRoutes;
    }
    
    
    
}
