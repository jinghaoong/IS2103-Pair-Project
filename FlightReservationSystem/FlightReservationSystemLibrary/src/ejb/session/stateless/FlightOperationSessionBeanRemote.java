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
import javax.ejb.Remote;
import util.exception.FareBasisCodeExistException;
import util.exception.FlightAlreadyExistException;
import util.exception.FlightNumberDisabledException;
import util.exception.FlightScheduleOverlapException;
import util.exception.ViolationException;

/**
 *
 * @author jinghao
 */
@Remote
public interface FlightOperationSessionBeanRemote {

    Flight createFlight(Flight newFlight) throws FlightAlreadyExistException, ViolationException;

    void updateFlight(Flight flight);
    
    void updateReturnAndFlight(Flight flight);

    List<Flight> retrieveAllFlights();

    Flight retrieveFlightByNumber(String flightNumber) throws FlightNumberDisabledException;

    String deleteFlight(Flight flight);

    String retrieveLargestFlightNumber();

    FlightSchedule createFlightSchedule(FlightSchedule flightSchedule) throws FlightScheduleOverlapException;

    FlightSchedule updateFlightSchedule(FlightSchedule flightSchedule);

    FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws ViolationException;

    FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);

    Fare createFare(Fare fare) throws FareBasisCodeExistException;

    void updateCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration);

    List<FlightSchedulePlan> retrieveAllMainFlightSchedulePlans();

    List<FlightReservation> retrieveFlightReservationsByCabinClass(CabinClassConfiguration cabinClass);

    void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);

    void updateFare(Fare newFare);

    List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();

}
