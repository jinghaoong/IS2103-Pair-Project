/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jinghao
 */
@Local
public interface FlightOperationSessionBeanLocal {

    void createFlight(Flight newFlight);

    void updateFlight(Flight flight);

    List<Flight> retrieveAllFlights();
    
}
