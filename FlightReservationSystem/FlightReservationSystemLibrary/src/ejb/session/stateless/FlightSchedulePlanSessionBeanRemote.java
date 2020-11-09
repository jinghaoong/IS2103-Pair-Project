/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jinghao
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {

    FlightSchedulePlan createSingleFlightSchedulePlan(String flightNumber, FlightSchedule flightSchedule, Fare fare);

    FlightSchedulePlan createMultipleFlightSchedulePlan(String flightNumber, List<Long> flightScheduleIds, List<Long> fareIds);

}
