/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author yylow
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public FlightRoute createFlightRoute(FlightRoute newFlightRoute) {
        
        em.persist(newFlightRoute);
        em.flush();
        
        return newFlightRoute;
    }
    
    @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        
        TypedQuery<FlightRoute> query = em.createQuery("SELECT f FROM FlightRoute f", FlightRoute.class);
        List<FlightRoute> flightRoutes = query.getResultList();
        // sort by ascending order
        flightRoutes.sort((FlightRoute fr1, FlightRoute fr2) -> fr1.getOriginAirport().getAirportName().compareTo(fr2.getOriginAirport().getAirportName()));
        // insert return flights if any
        int numOfRoutes = flightRoutes.size();
        for (int i = 0; i < numOfRoutes; i++) {
            FlightRoute route1 = flightRoutes.get(i);
            if (route1.getReturnFlightRoute() != null) { //  return flight route exists
                for (int j = i+1; j < numOfRoutes; j++) {
                    if (route1.getReturnFlightRoute().equals(flightRoutes.get(j))) {
                        FlightRoute returnRoute = flightRoutes.get(j);
                        flightRoutes.remove(j);
                        flightRoutes.add(i+1, returnRoute);
                    }
                }
            }
        }
        
        return flightRoutes;
    }

    @Override
    public void deleteFlightRoute(Long flightRouteId) {
        
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
        List<Flight> flights = flightRoute.getFlights();
        if (flights.isEmpty()) {
            if (flightRoute.getReturnFlightRoute() != null) {
                flightRoute.getReturnFlightRoute().setReturnFlightRoute(null);
                // persist?
            }
            em.remove(flightRoute);
        } else {
            // disassociate with other flights?
             flightRoute.setEnabled(false);
        }
    }
    
}
