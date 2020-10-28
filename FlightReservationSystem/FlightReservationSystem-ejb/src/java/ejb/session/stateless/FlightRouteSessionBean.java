/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
    public List<FlightRoute> viewAllFlightRoutes() {
        
        TypedQuery<FlightRoute> query = em.createQuery("SELECT a FROM AircraftConfiguration a", FlightRoute.class);
        List<FlightRoute> flightRoutes = query.getResultList();
        
        return flightRoutes;
    }

    @Override
    public void deleteFlightRoute(FlightRoute flightRoute) {
        
        em.remove(flightRoute);
    }
    
}
