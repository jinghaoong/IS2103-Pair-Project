/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidCredentialsException;
import util.exception.UsernameAlreadyTakenException;

/**
 *
 * @author yylow
 */
@Remote
public interface CustomerSessionBeanRemote {

    public Long createCustomer(Customer newCustomer) throws UsernameAlreadyTakenException;

    public Customer login(String username, String password) throws InvalidCredentialsException;

    public List<FlightReservation> retrieveFlightReservations(Long customerId);
    
}
