/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author yylow
 */
@Remote
public interface FlightRouteSessionBeanRemote {

    public FlightRoute createFlightRoute(FlightRoute newFlightRoute);

    public List<FlightRoute> retrieveAllFlightRoutes();

    public void deleteFlightRoute(Long flightRouteId);
    
}
