/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FareBasisCodeExistException;
import util.exception.FlightAlreadyExistException;
import util.exception.FlightNumberDisabledException;
import util.exception.FlightScheduleOverlapException;
import util.exception.ViolationException;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightOperationSessionBean implements FlightOperationSessionBeanRemote, FlightOperationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    @EJB
    private ValidatorSessionBeanLocal validatorSessionBeanLocal; 
    /*
    Flight
    */
    
    @Override
    public Flight createFlight(Flight newFlight) throws FlightAlreadyExistException, ViolationException {
        
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", newFlight.getFlightNumber());
        
        try {
            query.getSingleResult();
            throw new FlightAlreadyExistException("Flight already exists!");
        }
        catch (NoResultException ex) {
            // continue
        }
        
        try {
            validatorSessionBeanLocal.validate(newFlight);
            
            FlightRoute flightRoute = em.find(FlightRoute.class, newFlight.getFlightRoute().getFlightRouteId());
            flightRoute.getFlights().add(newFlight);

            em.persist(newFlight);
            em.flush();

            return newFlight;
        }
        catch (ViolationException ex) {
            throw new ViolationException(ex.getMessage());
        }
    }
    
    @Override
    public void updateFlight(Flight flight) {
        em.merge(flight);
    }
    
    @Override
    public void updateReturnAndFlight(Flight flight) {
        
        Flight returnFlight = em.find(Flight.class, flight.getReturnFlight().getFlightId());
        
        returnFlight.setAircraftConfig(flight.getAircraftConfig());
        
        em.merge(flight);
        em.merge(returnFlight);
    }
    
    @Override
    public List<Flight> retrieveAllFlights() {
        
        Query query = em.createQuery("SELECT f FROM Flight AS f");
        List<Flight> flights = query.getResultList();
        
        /*
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            if (f.getReturnFlight() != null) {
                flights.remove(f.getReturnFlight());
            }
        }
        */
        
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
    public Flight retrieveFlightByNumber(String flightNumber) throws FlightNumberDisabledException {
        
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", flightNumber);
        
        try {
            Flight flight = (Flight)query.getSingleResult();
            
            if (!flight.getEnabled()) {
                throw new FlightNumberDisabledException();
            }
            
            flight.getFlightSchedules().size();
            
            for (FlightSchedule fs : flight.getFlightSchedules()) {
                fs.getSeatInventories().size();
                fs.getFlightReservations().size();
            }
            
            flight.getAircraftConfig().getCabinClassConfigs().size();
            flight.getFlightRoute().getOriginAirport().getTimeZone();
            flight.getFlightRoute().getDestinationAirport().getTimeZone();
            flight.getReturnFlight();
            
            return flight;
        }
        catch (NoResultException | FlightNumberDisabledException ex) {
            throw new FlightNumberDisabledException("Flight with given flight number does not exist / is disabled!");
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
            if (flightSchedule.getDepartureDateTime().before(fs.getArrivalDateTime()) || flightSchedule.getArrivalDateTime().before(fs.getDepartureDateTime())) {
                //System.out.println(fs.getDepartureDateTime().toString());
                throw new FlightScheduleOverlapException("Flight Schedules for same Flight overlap!");
            }
        }
        
        flightSchedule.getFlightReservations().size();
        
        //Cascaded
        //em.persist(flightSchedule);
        //em.flush();
        
        return flightSchedule;
    }

    @Override
    public FlightSchedule updateFlightSchedule(FlightSchedule flightSchedule) {
        
        em.merge(flightSchedule);
        em.flush();
        
        return flightSchedule;
    }

    @Override
    public FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws ViolationException {
        
        try {
            Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
            query.setParameter("flightNumber", flightSchedulePlan.getFlightNumber());
            
            Flight flight = (Flight)query.getSingleResult();
            
            validatorSessionBeanLocal.validate(flightSchedulePlan);
            flightSchedulePlan.getStartDate();
            flightSchedulePlan.getEnabled();
            flightSchedulePlan.getFlightSchedules();
            flightSchedulePlan.getFares();
            
            for (FlightSchedule fs : flightSchedulePlan.getFlightSchedules()) {
                flight.getFlightSchedules().add(fs);
            }
            
            em.persist(flightSchedulePlan);
            em.merge(flight);
            em.flush();

            return flightSchedulePlan;
        }
        catch (ViolationException ex) {
            throw new ViolationException(ex.getMessage());
        }
        
    }

    @Override
    public FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        em.merge(flightSchedulePlan);
        em.flush();
        
        return flightSchedulePlan;
    }

    @Override
    public Fare createFare(Fare fare) throws FareBasisCodeExistException {
        
        Query query = em.createQuery("SELECT f FROM Fare f WHERE f.fareBasisCode = :fareBasisCode AND f.flightSchedulePlan = :flightSchedulePlan");
        query.setParameter("fareBasisCode", fare.getFareBasisCode());
        query.setParameter("flightSchedulePlan", fare.getFlightSchedulePlan());
        
        try {
            query.getSingleResult();
            throw new FareBasisCodeExistException("Fare basis code already exists in Flight Schedule Plan!");
        }
        catch (NoResultException ex) {
            // continue
        }
        return fare;
    }

    @Override
    public void updateCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) {
        em.merge(cabinClassConfiguration);
    }

    @Override
    public List<FlightSchedulePlan> retrieveAllMainFlightSchedulePlans() {
        
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan AS fsp");
        
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        List<FlightSchedulePlan> remove = new ArrayList<>();
        
        /*
        for (int i = 0; i < flightSchedulePlans.size(); i++) {
            FlightSchedulePlan fsp = flightSchedulePlans.get(i);
            
            if (fsp.getReturnFlightSchedulePlan() != null) {
                flightSchedulePlans.remove(fsp.getReturnFlightSchedulePlan());
            }
            
            fsp.getReturnFlightSchedulePlan();
            fsp.getFlightSchedules().size();
            fsp.getFares().size();
            
            for (FlightSchedule fs : fsp.getFlightSchedules()) {
                fs.getFlightReservations().size();
            }
        }
        */
        
        for (FlightSchedulePlan fsp : flightSchedulePlans) {
            if (fsp.getReturnFlightSchedulePlan() != null) {
                remove.add(fsp.getReturnFlightSchedulePlan());
            }
        }
        
        for (FlightSchedulePlan rfsp : remove) {
            flightSchedulePlans.remove(rfsp);
        }
        
        flightSchedulePlans.sort((FlightSchedulePlan fsp1, FlightSchedulePlan fsp2) -> fsp1.getStartDate().compareTo(fsp2.getStartDate()));
        flightSchedulePlans.sort((FlightSchedulePlan fsp1, FlightSchedulePlan fsp2) -> fsp1.getFlightNumber().compareTo(fsp2.getFlightNumber()));
        
        for (FlightSchedulePlan fsp : flightSchedulePlans) {
            fsp.getFlightSchedules().size();
            fsp.getReturnFlightSchedulePlan();
            fsp.getFares().size();
            
            for (FlightSchedule fs : fsp.getFlightSchedules()) {
                fs.getFlight().getFlightRoute().getOriginAirport();
                fs.getFlight().getFlightRoute().getDestinationAirport();
                fs.getFlightReservations().size();
            }
        }
        
        return flightSchedulePlans;
        
    }

    @Override
    public List<FlightReservation> retrieveFlightReservationsByCabinClass(CabinClassConfiguration cabinClass) {
        
        Query query = em.createQuery("SELECT fr FROM FlightReservation fr WHERE fr.cabinClassConfig = :cabinClass");
        query.setParameter("cabinClass", cabinClass);
        
        List<FlightReservation> flightReservations = query.getResultList();
        
        for (FlightReservation fr : flightReservations) {
            fr.getCustomer();
        }
        
        return flightReservations;
    }

    @Override
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, flightSchedulePlan.getFlightSchedulePlanId());
        
        em.remove(fsp);
    }

    @Override
    public void updateFare(Fare newFare) {
        em.merge(newFare);
    }

    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans() {
        
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan AS fsp");
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        
        for (FlightSchedulePlan fsp : flightSchedulePlans) {
            fsp.getFlightSchedules().size();
            fsp.getReturnFlightSchedulePlan();
            fsp.getFares().size();
            
            for (FlightSchedule fs : fsp.getFlightSchedules()) {
                fs.getFlight().getFlightRoute().getOriginAirport();
                fs.getFlight().getFlightRoute().getDestinationAirport();
                fs.getFlightReservations().size();
            }
        }
        
        return flightSchedulePlans;
    }
    
}
