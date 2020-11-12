/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteAlreadyExistException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author jinghao
 */
@Remote
public interface FlightPlanningSessionBeanRemote {

    List<AircraftType> retrieveAllAircraftTypes();

    void createAircraftConfig(AircraftConfiguration newAircraftConfig);

    List<AircraftConfiguration> retrieveAllAircraftConfigs();

    Airport retrieveAirportByCode(String airportCode) throws AirportNotFoundException;

    FlightRoute createFlightRoute(Airport originAirport, Airport destinationAirport) throws FlightRouteAlreadyExistException;

    void updateFlightRoute(FlightRoute flightRoute);

    List<FlightRoute> retrieveAllFlightRoutes();

    FlightRoute retrieveFlightRouteByCodes(String originAirportCode, String destinationAirportCode) throws FlightRouteNotFoundException;

    String deleteFlightRouteById(Long flightRouteId);

}
