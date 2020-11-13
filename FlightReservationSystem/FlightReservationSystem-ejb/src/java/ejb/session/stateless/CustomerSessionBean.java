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
        List<Customer> c1 = query.getResultList();
        query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail")
                .setParameter("inEmail", newCustomer.getEmail());
        List<Customer> c2 = query.getResultList();
        query = em.createQuery("SELECT c FROM Customer c WHERE c.mobileNumber = :inMobileNumber")
                .setParameter("inMobileNumber", newCustomer.getMobileNumber());
        List<Customer> c3 = query.getResultList();
        
        if (!c1.isEmpty()) {
            throw new UsernameAlreadyTakenException("Username is already taken. Please try again.\n");
        } else if (!c2.isEmpty()) {
            throw new EmailAlreadyInUseException("An account with the email is already in use. Please try again.\n");
        } else if (!c3.isEmpty()) {
            throw new MobileNumberAlreadyInUseException("An account with the mobile number is already in use. Please try again.\n");
        } else {
            em.persist(newCustomer);
            em.flush();
        }
        
        return newCustomer.getCustomerId();
    }

    @Override
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
