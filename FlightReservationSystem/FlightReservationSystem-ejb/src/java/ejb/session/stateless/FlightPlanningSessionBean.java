/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteAlreadyExistException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightPlanningSessionBean implements FlightPlanningSessionBeanRemote, FlightPlanningSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    /*
    Fleet Manager methods
    */
    @Override
    public List<AircraftType> retrieveAllAircraftTypes() {
        
        Query query = em.createQuery("SELECT at FROM AircraftType AS at");
        return query.getResultList();
    }

    @Override
    public void createAircraftConfig(AircraftConfiguration newAircraftConfig) {
        
        Long id = newAircraftConfig.getAircraftType().getAircraftTypeId();
        
        AircraftType acType = em.find(AircraftType.class, id);
        acType.getAircraftConfigurations().add(newAircraftConfig);
        
        for (CabinClassConfiguration cc : newAircraftConfig.getCabinClassConfigs()) {
            cc.setAircraftConfig(newAircraftConfig);
            em.persist(cc);
        }
        
        em.persist(newAircraftConfig);
    }

    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigs() {
        
        TypedQuery<AircraftConfiguration> query = em.createQuery("SELECT a FROM AircraftConfiguration a", AircraftConfiguration.class);
        List<AircraftConfiguration> aircraftConfigs = query.getResultList();
        // sort by aircraft type
        aircraftConfigs.sort((AircraftConfiguration ac1, AircraftConfiguration ac2) -> 
                    ac1.getAircraftConfigName().compareTo(ac2.getAircraftConfigName()));
        aircraftConfigs.sort((AircraftConfiguration ac1, AircraftConfiguration ac2) -> 
                    ac1.getAircraftType().getAircraftTypeName().compareTo(ac2.getAircraftType().getAircraftTypeName()));
        
        for (AircraftConfiguration ac : aircraftConfigs) {
            ac.getCabinClassConfigs().size();
        }
        
        return aircraftConfigs;
    }
    
    /*
    Route Planner Methods
    */

    @Override
    public Airport retrieveAirportByCode(String airportCode) throws AirportNotFoundException {
        
        Query query = em.createQuery("SELECT a FROM Airport a WHERE a.airportCode = :airportCode");
        query.setParameter("airportCode", airportCode);
        
        try {
            Airport airport = (Airport)query.getSingleResult();
            airport.getDestinationFlightRoutes().size();
            airport.getOriginFlightRoutes().size();
            return airport;
        }
        catch (NoResultException ex) {
            throw new AirportNotFoundException("Airport with given IATA code not found!");
        }
    }

    @Override
    public FlightRoute createFlightRoute(Airport originAirport, Airport destinationAirport) throws FlightRouteAlreadyExistException {
        
        String originCode = originAirport.getAirportCode();
        String destinationCode = destinationAirport.getAirportCode();
        
        try {
            FlightRoute flightRoute = retrieveFlightRouteByCodes(originCode, destinationCode);
            throw new FlightRouteAlreadyExistException("Flight Route already exists!");
        } catch (FlightRouteNotFoundException ex) {
            FlightRoute flightRoute = new FlightRoute(originAirport, destinationAirport);
            flightRoute.getFlights().size();
            
            em.persist(flightRoute);
            em.flush();
            
            return flightRoute;
        }
    }

    @Override
    public void updateFlightRoute(FlightRoute flightRoute) {
        em.merge(flightRoute);
    }

    @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        
        Query query = em.createQuery("SELECT fr FROM FlightRoute AS fr");
        List<FlightRoute> flightRoutes = query.getResultList();
        List<FlightRoute> temp = new ArrayList<>();
        
        for (int i = 0; i < flightRoutes.size(); i++) {
            FlightRoute fr = flightRoutes.get(i);
            
            if (fr.getReturnFlightRoute() != null) {
                flightRoutes.remove(fr.getReturnFlightRoute());
            }
        }
        
        flightRoutes.sort((FlightRoute fr1, FlightRoute fr2) -> fr1.getOriginAirport().getAirportName().compareTo(fr2.getOriginAirport().getAirportName()));
        
        for (FlightRoute fr : flightRoutes) {
            fr.getOriginAirport();
            fr.getDestinationAirport();
            fr.getFlights().size();
            fr.getReturnFlightRoute();
        }
        
        return flightRoutes;
    }

    @Override
    public FlightRoute retrieveFlightRouteByCodes(String originAirportCode, String destinationAirportCode) throws FlightRouteNotFoundException {
        
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.originAirport.airportCode = :originCode AND fr.destinationAirport.airportCode = :destinationCode");
        query.setParameter("originCode", originAirportCode);
        query.setParameter("destinationCode", destinationAirportCode);
        
        try {
            return (FlightRoute)query.getSingleResult();
        }
        catch (NoResultException ex) {
            throw new FlightRouteNotFoundException("Flight Route not found!");
        }
    }

    @Override
    public String deleteFlightRouteById(Long flightRouteId) {
        
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
        
        if (flightRoute.getFlights().isEmpty()) {
            if (flightRoute.getReturnFlightRoute() != null) {
                flightRoute.getReturnFlightRoute().setReturnFlightRoute(null);
            }
            em.remove(flightRoute);
            return "Flight Route successfully deleted!";
        }
        else {
            flightRoute.setEnabled(Boolean.FALSE);
            return "Flight Route disabled.";
        }
        
    }
    
}
