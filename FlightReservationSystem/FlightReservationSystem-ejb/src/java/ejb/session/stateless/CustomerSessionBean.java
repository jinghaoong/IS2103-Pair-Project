/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Flight;
import entity.FlightReservation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmailAlreadyInUseException;
import util.exception.InvalidCredentialsException;
import util.exception.MobileNumberAlreadyInUseException;
import util.exception.NoFlightReservationsMadeException;
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

        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername")
                .setParameter("inUsername", username);
        
        Customer customer = new Customer();
        try {
            customer = (Customer) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("An account with this username does not exist!\n");
            return null;
        }
        
        if (customer.getPassword().equals(password)) {
            return customer;
        } else {
            throw new InvalidCredentialsException("The username or password is incorrect, please try again!");
        }
    }

    public List<Flight> makeSearch() {
        return new ArrayList<>();
    }

    @Override
    public List<FlightReservation> retrieveFlightReservations(Long customerId) throws NoFlightReservationsMadeException {

        Customer customer = em.find(Customer.class, customerId);
        Query query = em.createQuery("SELECT f in FlightReservation f WHERE f.customer = :inCustomer")
                .setParameter("inCustomer", customer);
        List<FlightReservation> flightReservations = query.getResultList();
        
        if (flightReservations.isEmpty()) {
            throw new NoFlightReservationsMadeException("No Flight Reservations have been made.");
        } else {
            return flightReservations;
        }
    }

}
