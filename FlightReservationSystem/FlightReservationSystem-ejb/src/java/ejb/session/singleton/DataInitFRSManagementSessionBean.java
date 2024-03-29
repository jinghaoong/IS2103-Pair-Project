/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.AircraftType;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRole;
import util.enumeration.PartnerRole;

/**
 *
 * @author jinghao
 */
@Singleton
@LocalBean
//@Startup
public class DataInitFRSManagementSessionBean {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em; 

    public DataInitFRSManagementSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        
        Employee employee = em.find(Employee.class, 1L);
                
        if (employee == null) {
            initData();
        }
        
    }
    
    private void initData() {
        
        try {
            String password = "password";
            
            // Employee Creation
            Employee systemAdmin = new Employee("System Administrator", "admin", password, EmployeeRole.SYSTEM_ADMIN);
            em.persist(systemAdmin);
            Employee fleetManager = new Employee("Fleet Manager", "fleetmanager", password, EmployeeRole.FLEET_MANAGER);
            em.persist(fleetManager);
            Employee routePlanner = new Employee("Route Planner", "routeplanner", password, EmployeeRole.ROUTE_PLANNER);
            em.persist(routePlanner);
            Employee scheduleManager = new Employee("Schedule Manager", "schedulemanager", password, EmployeeRole.SCHEDULE_MANAGER);
            em.persist(scheduleManager);
            Employee salesManager = new Employee("Sales Manager", "salesmanager", password, EmployeeRole.SALES_MANAGER);
            em.persist(salesManager);
            em.flush();
            
            // Partner Creation            
            Partner partnerEmployee = new Partner("Partner Employee", "partneremployee", password, PartnerRole.EMPLOYEE);
            em.persist(partnerEmployee);
            Partner partnerReservationManager = new Partner("Partner Reservation Manager", "partnermanager", password, PartnerRole.RESERVATION_MANAGER);
            em.persist(partnerReservationManager);
            em.flush();
            
            // Airport Creation
            Airport changiAirport = new Airport("Changi International Airport", "SIN", "Singapore", "Singapore", "Singapore", +8);
            changiAirport.getOriginFlightRoutes().size();
            changiAirport.getDestinationFlightRoutes().size();
            em.persist(changiAirport);
            Airport hongKongAirport = new Airport("Hong Kong International Airport", "HKG", "Hong Kong", "Hong Kong", "Hong Kong", +8);
            hongKongAirport.getOriginFlightRoutes().size();
            hongKongAirport.getDestinationFlightRoutes().size();
            em.persist(hongKongAirport);
            Airport jfkAirport = new Airport("John F. Kennedy International Airport", "JFK", "New York", "New York", "USA", -5);
            jfkAirport.getOriginFlightRoutes().size();
            jfkAirport.getDestinationFlightRoutes().size();
            em.persist(jfkAirport);
            em.flush();
            
            // Aircraft Type Creation    
            AircraftType boeing737 = new AircraftType("Boeing 737", 204);
            boeing737.getAircraftConfigurations().size();
            em.persist(boeing737);
            AircraftType boeing747 = new AircraftType("Boeing 747", 467);
            boeing747.getAircraftConfigurations().size();
            em.persist(boeing747);
            em.flush();
        }
        catch(Exception e) { // temp
            System.err.println(e);
        }
    }

}
