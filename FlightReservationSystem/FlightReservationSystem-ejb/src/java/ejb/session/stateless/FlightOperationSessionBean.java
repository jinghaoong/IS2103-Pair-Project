/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FareBasisCodeExistException;
import util.exception.FlightNumberDoesNotExistException;
import util.exception.FlightScheduleOverlapException;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightOperationSessionBean implements FlightOperationSessionBeanRemote, FlightOperationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    /*
    Flight
    */
    
    @Override
    public Flight createFlight(Flight newFlight) {
        
        FlightRoute flightRoute = em.find(FlightRoute.class, newFlight.getFlightRoute().getFlightRouteId());
        flightRoute.getFlights().add(newFlight);
        
        em.persist(newFlight);
        em.flush();
        
        return newFlight;
    }
    
    @Override
    public void updateFlight(Flight flight) {
        em.merge(flight);
    }

    @Override
    public List<Flight> retrieveAllFlights() {
        
        Query query = em.createQuery("SELECT f FROM Flight AS f");
        List<Flight> flights = query.getResultList();
        
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            if (f.getReturnFlight() != null) {
                flights.remove(f.getReturnFlight());
            }
        }
        
        flights.sort((Flight f1, Flight f2) -> f1.getFlightNumber().compareTo(f2.getFlightNumber()));
        
        for (Flight f : flights) {
            f.getAircraftConfig().getCabinClassConfigs().size();
            f.getAircraftConfig().getAircraftType();
            f.getFlightSchedules().size();
            f.getFlightRoute().getFlights().size();
        }
        
        return flights;
    }

    @Override
    public Flight retrieveFlightByNumber(String flightNumber) throws FlightNumberDoesNotExistException {
        
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", flightNumber);
        
        try {
            Flight flight = (Flight)query.getSingleResult();
            flight.getFlightSchedules().size();
            
            return flight;
        }
        catch (NoResultException ex) {
            throw new FlightNumberDoesNotExistException("Flight with given flight number does not exist!");
        }
    }
    
    @Override
    public String deleteFlight(Flight flight) {
        
        Flight f = em.find(Flight.class, flight.getFlightId());
        
        if (f.getFlightSchedules().isEmpty()) {
            f.getFlightRoute().getFlights().remove(f);
            em.remove(f);
            return "Flight has been successfully deleted!";
        }
        
        f.setEnabled(Boolean.FALSE);
        return "Flight has been disabled as it is still in use.";
    }

    @Override
    public String retrieveLargestFlightNumber() {
        
        Query query = em.createQuery("SELECT f FROM Flight AS f");
        List<Flight> flights = query.getResultList();
        
        flights.sort((Flight f1, Flight f2) -> f2.getFlightNumber().compareTo(f1.getFlightNumber()));
                
        return flights.get(0).getFlightNumber();
    }
    
    /*
    Flight Schedule Plan
    */

    @Override
    public FlightSchedule createFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleOverlapException {
        
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flight.flightNumber = :flightNumber");
        query.setParameter("flightNumber", flightSchedule.getFlight().getFlightNumber());
        
        List<FlightSchedule> flightSchedules = query.getResultList();
        
        for (FlightSchedule fs : flightSchedules) {
            if (flightSchedule.getDepartureDateTime().before(fs.getArrivalDateTime()) || flightSchedule.getArrivalDateTime().after(fs.getDepartureDateTime())) {
                throw new FlightScheduleOverlapException("Flight Schedules for same Flight overlap!");
            }
        }
        
        flightSchedule.getFlightReservations().size();
        em.persist(flightSchedule);
        em.flush();
        
        return flightSchedule;
    }

    @Override
    public FlightSchedule updateFlightSchedule(FlightSchedule flightSchedule) {
        
        em.merge(flightSchedule);
        em.flush();
        
        return flightSchedule;
    }

    @Override
    public FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        flightSchedulePlan.getFlightSchedules().size();
        em.persist(flightSchedulePlan);
        em.flush();
        
        return flightSchedulePlan;
    }

    @Override
    public FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        em.merge(flightSchedulePlan);
        em.flush();
        
        return flightSchedulePlan;
    }

    @Override
    public Fare createFare(Fare fare) throws FareBasisCodeExistException {
        
        Query query = em.createQuery("SELECT f FROM Fare f WHERE f.fareBasisCode = :fareBasisCode");
        query.setParameter("fareBasisCode", fare.getFareBasisCode());
        
        try {
            query.getSingleResult();
            throw new FareBasisCodeExistException("Fare basis code already exists!");
        }
        catch (NoResultException ex) {
            em.persist(fare);
            em.flush();
            
            return fare;
        }
    }

    @Override
    public void updateCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) {
        em.merge(cabinClassConfiguration);
    }
    
}
