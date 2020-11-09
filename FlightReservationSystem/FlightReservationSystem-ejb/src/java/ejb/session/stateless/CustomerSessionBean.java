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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidCredentialsException;
import util.exception.UsernameAlreadyTakenException;

/**
 *
 * @author yylow
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public Long createCustomer(Customer newCustomer) throws UsernameAlreadyTakenException { // throws AlreadyLoggedInException? can handle at client side actually
        
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername")
                .setParameter("inUsername", newCustomer.getUsername());
        Customer c = (Customer) query.getSingleResult();
        
        if (c != null) {
            throw new UsernameAlreadyTakenException("This Username is already taken, please try another!");
        } else {
            em.persist(newCustomer);
            em.flush();
        }
        
        return newCustomer.getCustomerId();
    }
    
    public Customer login(String username, String password) throws InvalidCredentialsException {
    
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername")
                .setParameter("inUsername", username);
        Customer customer = (Customer) query.getSingleResult(); // can catch here also
        
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
    public List<FlightReservation> retrieveFlightReservations(Long customerId) {
        
        Customer customer = em.find(Customer.class, customerId);
        return customer.getFlightReservations();
    }
    
}
