/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Flight createFlight(Flight flight) {
       
        em.persist(flight);
        em.flush();
        
        return flight;
    }
    
    @Override
    public List<Flight> retrieveAllFlights() {
        
        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f", Flight.class);
        List<Flight> flights = query.getResultList();
        
        flights.sort((Flight f1, Flight f2) -> f1.getFlightNumber().compareTo(f2.getFlightNumber()));

        return flights;
    }

    @Override
    public Flight retrieveFlightRecord(String flightNumber) {
        
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", flightNumber);
        Flight flight = (Flight)query.getSingleResult();
        
        return flight;
    }

    @Override
    public void updateFlight(String flightNumber, FlightRoute flightRoute, AircraftConfiguration aircraftConfig) {
        
        Flight flight = retrieveFlightRecord(flightNumber);
        
        if (flight.getFlightRoute() != flightRoute) {
        
        }
        flight.setFlightRoute(flightRoute);
        flight.setAircraftConfig(aircraftConfig);
    }

    @Override
    public void deleteFlight(String flightNumber) {
        
        Flight flight = retrieveFlightRecord(flightNumber);
        
        em.remove(flight);
    }

    
    
}
