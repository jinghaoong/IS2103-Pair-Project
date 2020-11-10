/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightOperationSessionBean implements FlightOperationSessionBeanRemote, FlightOperationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createFlight(Flight newFlight) {
        newFlight.getFlightSchedules().size();
        em.persist(newFlight);
    }

    @Override
    public void updateFlight(Flight flight) {
        em.merge(flight);
    }

    @Override
    public List<Flight> retrieveAllFlights() {
        
        Query query = em.createQuery("SELECT f FROM Flight AS f");
        List<Flight> flights = query.getResultList();
        
        for (Flight f : flights) {
            if (f.getReturnFlight() != null) {
                flights.remove(f.getReturnFlight());
            }
        }
        
        flights.sort((Flight f1, Flight f2) -> f1.getFlightNumber().compareTo(f2.getFlightNumber()));
        
        return flights;
    }
    
}
