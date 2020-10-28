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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.FlightScheduleType;

/**
 *
 * @author jinghao
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public FlightSchedulePlan createSingleFlightSchedulePlan(String flightNumber, FlightSchedule flightSchedule, Fare fare) {
        
        FlightSchedulePlan singleFlightSchedulePlan = new FlightSchedulePlan(flightNumber, FlightScheduleType.SINGLE);
        
        singleFlightSchedulePlan.addFlightSchedule(flightSchedule);
        flightSchedule.setFlightSchedulePlan(singleFlightSchedulePlan);
        singleFlightSchedulePlan.addFare(fare);
        
        em.persist(singleFlightSchedulePlan);
        em.persist(flightSchedule);
        em.flush();
        
        return singleFlightSchedulePlan;
    }

    @Override
    public FlightSchedulePlan createMultipleFlightSchedulePlan(String flightNumber, List<Long> flightScheduleIds, List<Long> fareIds) {
        
        FlightSchedulePlan multipleFlightSchedulePlan = new FlightSchedulePlan(flightNumber, FlightScheduleType.MULTIPLE);
        
        for (Long flightScheduleId : flightScheduleIds) {
            FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
            
            multipleFlightSchedulePlan.addFlightSchedule(flightSchedule);
            flightSchedule.setFlightSchedulePlan(multipleFlightSchedulePlan);
            
            em.persist(flightSchedule);
            em.flush();
        }
        
        em.persist(multipleFlightSchedulePlan);
        em.flush();
        
        return multipleFlightSchedulePlan;
    }
    
}
