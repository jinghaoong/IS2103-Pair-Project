/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import com.sun.xml.wss.util.DateUtils;
import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Customer;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.DATE;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClass;
import util.exception.EmailAlreadyInUseException;
import util.exception.InvalidCredentialsException;
import util.exception.MobileNumberAlreadyInUseException;
import util.exception.UsernameAlreadyTakenException;

/**
 *
 * @author yylow
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createCustomer(Customer newCustomer) throws UsernameAlreadyTakenException, EmailAlreadyInUseException, MobileNumberAlreadyInUseException  {

        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername")
                .setParameter("inUsername", newCustomer.getUsername());
        try {
            Customer customer = (Customer) query.getSingleResult();
            throw new UsernameAlreadyTakenException("This username has already been taken, please try again.");
        } catch (NoResultException ex1) {
            
            query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail")
                .setParameter("inEmail", newCustomer.getEmail());
            try {
                Customer customer = (Customer) query.getSingleResult();
                throw new EmailAlreadyInUseException("An account with this email is already in use!");
            } catch (NoResultException ex2) {
            
                query = em.createQuery("SELECT c FROM Customer c WHERE c.mobileNumber = :inMobileNumber")
                .setParameter("inMobileNumber", newCustomer.getMobileNumber());
                try {
                    Customer customer = (Customer) query.getSingleResult();
                    throw new MobileNumberAlreadyInUseException("An account with this mobile number is already in use!");
                } catch (NoResultException ex) {

                    em.persist(newCustomer);
                    em.flush();
        
                    return newCustomer.getCustomerId();
                }
            }
        }
    }

    @Override
    public Customer login(String username, String password) throws InvalidCredentialsException {

        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername AND c.password = :inPassword")
                .setParameter("inUsername", username).setParameter("inPassword", password);
        
        try {
            Customer customer = (Customer) query.getSingleResult();
            return customer;
        } catch (NoResultException ex) {
            throw new InvalidCredentialsException("The username or password is incorrect, please try again.");
        }
    }

    
    @Override
    public List<FlightSchedule> makeDirectSearch(String departureAirport, String destinationAirport, Date departureDate,
            Integer numOfPassengers, String cabinClass) {

        // all cabin classes
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDepartureDate AND fs.flight.flightRoute.originAirport.airportName = :inDepartureAirport AND fs.flight.flightRoute.destinationAirport.airportName = :inDestinationAirport")
                .setParameter("inDepartureAirport", departureAirport)
                .setParameter("inDestinationAirport", destinationAirport)
                .setParameter("inDepartureDate", departureDate);
        
        CabinClass cabClass = CabinClass.valueOf("F");
        if (cabinClass.equals("F") || cabinClass.equals("J") || cabinClass.equals("W") || cabinClass.equals("L")) { // if customer has a preference
            cabClass = CabinClass.valueOf(cabinClass);
            
            try {
                List<FlightSchedule> flightSchedules = query.getResultList();
                for (FlightSchedule fs : flightSchedules) {
                    Flight flight = fs.getFlight();
                    fs.getFlight().getAircraftConfig().getCabinClassConfigs().size();
                    fs.getSeatInventories().size();
                    if (!enoughSeatsInCabin(fs, cabClass, numOfPassengers)) {
                        flightSchedules.remove(fs);
                    }
                }
                return flightSchedules;
            } catch (NoResultException ex) {
                return new ArrayList<>();
            }
            
        } else { // no preference
            try {
                List<FlightSchedule> flightSchedules = query.getResultList();
                for (FlightSchedule fs : flightSchedules) {
                    Integer availableSeats = 0;
                    fs.getSeatInventories().size();
                    List<SeatInventory> seatInventories = fs.getSeatInventories();
                    for (SeatInventory si : seatInventories) {
                        availableSeats += si.getAvailable();
                    }
                    if (availableSeats <= numOfPassengers) {
                        flightSchedules.remove(fs);
                    }
                }
                return flightSchedules;
            } catch (NoResultException ex) {
                return new ArrayList<>();
            }
        }
    }
    
    @Override
    public Boolean enoughSeatsInCabin(FlightSchedule flightSchedule, CabinClass cabinClass, Integer numOfPassengers) {
        
        Flight flight = flightSchedule.getFlight();
        AircraftConfiguration aircraftConfig = flight.getAircraftConfig();
        List<CabinClassConfiguration> cabinConfigs = aircraftConfig.getCabinClassConfigs();
        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
        
        for (int i = 0; i < cabinConfigs.size(); i++) {
            if (cabinConfigs.get(i).getCabinClass() == cabinClass && seatInventories.get(i).getAvailable() >= numOfPassengers) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<FlightSchedule> makeConnectingSearch(String departureAirport, String destinationAirport, Date previousFlightArrivalDateTime,
            Integer numOfPassengers, String cabinClass) {
        
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flight.flightRoute.originAirport.airportName = :inDepartureAirport AND fs.flight.flightRoute.destinationAirport.airportName = :inDestinationAirport")
                .setParameter("inDepartureAirport", departureAirport)
                .setParameter("inDestinationAirport", destinationAirport);
        
        CabinClass cabClass = CabinClass.valueOf("F");
        if (cabinClass.equals("F") || cabinClass.equals("J") || cabinClass.equals("W") || cabinClass.equals("L")) { // if customer has a preference
            cabClass = CabinClass.valueOf(cabinClass);
            
            try {
                List<FlightSchedule> flightSchedules = query.getResultList();
                for (FlightSchedule fs : flightSchedules) {
                    Flight flight = fs.getFlight();
                    fs.getFlight().getAircraftConfig().getCabinClassConfigs().size();
                    fs.getSeatInventories().size();
                    if (fs.getDepartureDateTime().before(previousFlightArrivalDateTime) || !enoughSeatsInCabin(fs, cabClass, numOfPassengers)) {
                        flightSchedules.remove(fs);
                    }
                }
                return flightSchedules;
            } catch (NoResultException ex) {
                return new ArrayList<>();
            }
        
        } else { // no preference
            
            try {
                List<FlightSchedule> flightSchedules = query.getResultList();
                for (FlightSchedule fs : flightSchedules) {
                    if (fs.getDepartureDateTime().before(previousFlightArrivalDateTime)) {
                        flightSchedules.remove(fs);
                    }
                    fs.getSeatInventories().size();
                    Integer availableSeats = 0;
                    fs.getSeatInventories().size();
                    List<SeatInventory> seatInventories = fs.getSeatInventories();
                    for (SeatInventory si : seatInventories) {
                        availableSeats += si.getAvailable();
                    }
                    if (availableSeats <= numOfPassengers) {
                        flightSchedules.remove(fs);
                    }
                }
                return flightSchedules;
            } catch (NoResultException ex) {
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<FlightReservation> retrieveFlightReservations(Long customerId) {

        Customer customer = em.find(Customer.class, customerId);
        Query query = em.createQuery("SELECT f FROM FlightReservation f WHERE f.customer = :inCustomer")
                .setParameter("inCustomer", customer);
        
        try {
            List<FlightReservation> flightReservations = query.getResultList();
            return flightReservations;
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

}
