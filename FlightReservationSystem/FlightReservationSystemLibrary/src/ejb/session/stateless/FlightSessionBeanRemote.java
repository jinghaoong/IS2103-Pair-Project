/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jinghao
 */
@Remote
public interface FlightSessionBeanRemote {

    Flight createFlight(Flight flight);

    List<Flight> retrieveAllFlights();

    Flight retrieveFlightRecord(String flightNumber);

    void updateFlight(String flightNumber, FlightRoute flightRoute, AircraftConfiguration aircraftConfig);

    void deleteFlight(String flightNumber);

}
