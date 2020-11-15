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
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;
import util.exception.FareBasisCodeExistException;
import util.exception.FlightAlreadyExistException;
import util.exception.FlightNumberDoesNotExistException;
import util.exception.FlightScheduleOverlapException;

/**
 *
 * @author jinghao
 */
@Local
public interface FlightOperationSessionBeanLocal {

    Flight createFlight(Flight newFlight) throws FlightAlreadyExistException;

    void updateFlight(Flight flight);
    
    void updateReturnAndFlight(Flight flight);

    List<Flight> retrieveAllFlights();

    Flight retrieveFlightByNumber(String flightNumber) throws FlightNumberDoesNotExistException;

    String deleteFlight(Flight flight);

    String retrieveLargestFlightNumber();

    FlightSchedule createFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleOverlapException;

    FlightSchedule updateFlightSchedule(FlightSchedule flightSchedule);

    FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);

    FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);

    Fare createFare(Fare fare) throws FareBasisCodeExistException;

    void updateCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration);

    List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();

    List<FlightReservation> retrieveFlightReservationsByCabinClass(CabinClassConfiguration cabinClass);

    void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);

}
