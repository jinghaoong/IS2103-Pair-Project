/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClass;
import static util.enumeration.CabinClass.*;
import util.enumeration.EmployeeRole;
import static util.enumeration.FlightScheduleType.*;
import util.enumeration.PartnerRole;

/**
 *
 * @author yylow
 */
@Singleton
@LocalBean
//@Startup
public class TestDataInitSessionBean {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

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
            
            // Employees
            Employee fleetManager = new Employee("Fleet Manager", "fleetmanager", password, EmployeeRole.FLEET_MANAGER);
            em.persist(fleetManager);
            Employee routePlanner = new Employee("Route Planner", "routeplanner", password, EmployeeRole.ROUTE_PLANNER);
            em.persist(routePlanner);
            Employee scheduleManager = new Employee("Schedule Manager", "schedulemanager", password, EmployeeRole.SCHEDULE_MANAGER);
            em.persist(scheduleManager);
            Employee salesManager = new Employee("Sales Manager", "salesmanager", password, EmployeeRole.SALES_MANAGER);
            em.persist(salesManager);
            em.flush();
            
            // Partner
            Partner holidaydotcom = new Partner("Holiday.com", "holidaydotcom", password, PartnerRole.RESERVATION_MANAGER); // unsure
            em.persist(holidaydotcom);
            em.flush();
            
            // Airport
            Airport changi = new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore", +8);
            changi.getOriginFlightRoutes().size();
            changi.getDestinationFlightRoutes().size();
            em.persist(changi);
            Airport hongkong = new Airport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China", +8);
            hongkong.getOriginFlightRoutes().size();
            hongkong.getDestinationFlightRoutes().size();
            em.persist(hongkong);
            Airport taoyuan = new Airport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.", +8);
            taoyuan.getOriginFlightRoutes().size();
            taoyuan.getDestinationFlightRoutes().size();
            em.persist(taoyuan);
            Airport narita = new Airport("Narita", "NRT", "Narita", "Chiba", "Japan", +9);
            narita.getOriginFlightRoutes().size();
            narita.getDestinationFlightRoutes().size();
            em.persist(narita);
            Airport sydney = new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", +11);
            sydney.getOriginFlightRoutes().size();
            sydney.getDestinationFlightRoutes().size();
            em.persist(sydney);
            em.flush();
            
            // Aircraft Type
            AircraftType b737 = new AircraftType("Boeing 737", 200);
            em.persist(b737);
            AircraftType b747 = new AircraftType("Boeing 747", 400);
            em.persist(b747);
            
            // Aircraft Configuration
            AircraftConfiguration aircraftConfig737ae = new AircraftConfiguration("Boeing 737 All Economy", 1, 180);
            Integer[] arr = {3,3};
            CabinClassConfiguration ccc1 = new CabinClassConfiguration(Y, 1, 30, 6, arr);
            aircraftConfig737ae.getCabinClassConfigs().add(ccc1);
            em.persist(aircraftConfig737ae);
            AircraftConfiguration aircraftConfig737tc = new AircraftConfiguration("Boeing 737 Three Classes", 3, 180);
            arr = new Integer[] {1,1};
            CabinClassConfiguration ccc2 = new CabinClassConfiguration(CabinClass.F, 1, 5, 2, arr);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc2);
            arr = new Integer[] {2,2};
            CabinClassConfiguration ccc3 = new CabinClassConfiguration(CabinClass.J, 1, 5, 4, arr);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc3);
            arr = new Integer[] {3,3};
            CabinClassConfiguration ccc4 = new CabinClassConfiguration(CabinClass.Y, 1, 25, 6, arr);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc4);
            em.persist(aircraftConfig737tc);
            AircraftConfiguration aircraftConfig747ae = new AircraftConfiguration("Boeing 747 All Economy", 1, 380);
            arr = new Integer[] {3,4,3};
            CabinClassConfiguration ccc5 = new CabinClassConfiguration(CabinClass.Y, 2, 38, 10, arr);
            aircraftConfig747ae.getCabinClassConfigs().add(ccc5);
            em.persist(aircraftConfig747ae);
            AircraftConfiguration aircraftConfig747tc = new AircraftConfiguration("Boeing 747 Three Classes", 3, 360);
            arr = new Integer[] {1,1};
            CabinClassConfiguration ccc6 = new CabinClassConfiguration(F, 1, 5, 2, arr);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc6);
            arr = new Integer[] {2,2};
            CabinClassConfiguration ccc7 = new CabinClassConfiguration(J, 1, 5, 4, arr);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc7);
            arr = new Integer[] {3,3};
            CabinClassConfiguration ccc8 = new CabinClassConfiguration(Y, 1, 25, 6, arr);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc8);
            em.persist(aircraftConfig747tc);
            em.flush();
            
            // Flight Route
            FlightRoute sinHkg = new FlightRoute(changi, hongkong);
            sinHkg.setReturnFlightRoute(new FlightRoute(hongkong, changi));
            em.persist(sinHkg);
            FlightRoute sinTpe = new FlightRoute(changi, taoyuan);
            sinTpe.setReturnFlightRoute(new FlightRoute(taoyuan, changi));
            em.persist(sinTpe);
            FlightRoute sinNrt = new FlightRoute(changi, narita);
            sinNrt.setReturnFlightRoute(new FlightRoute(narita, changi));
            em.persist(sinNrt);
            FlightRoute hkgNrt = new FlightRoute(hongkong, narita);
            hkgNrt.setReturnFlightRoute(new FlightRoute(narita, hongkong));
            em.persist(hkgNrt);
            FlightRoute tpeNrt = new FlightRoute(taoyuan, narita);
            tpeNrt.setReturnFlightRoute(new FlightRoute(narita, taoyuan));
            em.persist(tpeNrt);
            FlightRoute sinSyd = new FlightRoute(changi, sydney);
            sinSyd.setReturnFlightRoute(new FlightRoute(sydney, changi));
            em.persist(sinSyd);
            FlightRoute sydNrt = new FlightRoute(sydney, narita);
            sydNrt.setReturnFlightRoute(new FlightRoute(narita, sydney));
            em.persist(sydNrt);
            em.flush();
            
            // Flights
            Flight ml111 = new Flight("ML111");
            FlightRoute flightRoute = new FlightRoute(changi, hongkong);
            ml111.setFlightRoute(flightRoute);
            ml111.setAircraftConfig(aircraftConfig737tc);
            Flight ml112 = new Flight("ML112");
            FlightRoute returnFlightRoute = new FlightRoute(hongkong, changi);
            ml112.setFlightRoute(returnFlightRoute);
            ml112.setAircraftConfig(aircraftConfig737tc);
            ml111.setReturnFlight(ml112);
            em.persist(ml111);
            em.persist(ml112);
            Flight ml211 = new Flight("ML211");
            flightRoute = new FlightRoute(changi, taoyuan);
            ml211.setFlightRoute(flightRoute);
            ml211.setAircraftConfig(aircraftConfig737tc);
            Flight ml212 = new Flight("ML212");
            returnFlightRoute = new FlightRoute(taoyuan, changi);
            ml212.setFlightRoute(returnFlightRoute);
            ml212.setAircraftConfig(aircraftConfig737tc);
            ml211.setReturnFlight(ml212);
            em.persist(ml211);
            em.persist(ml212);
            Flight ml311 = new Flight("ML311");
            flightRoute = new FlightRoute(changi, narita);
            ml311.setFlightRoute(flightRoute);
            ml311.setAircraftConfig(aircraftConfig747tc);
            Flight ml312 = new Flight("ML312");
            returnFlightRoute = new FlightRoute(narita, changi);
            ml312.setFlightRoute(returnFlightRoute);
            ml312.setAircraftConfig(aircraftConfig747tc);
            ml311.setReturnFlight(ml312);
            em.persist(ml311);
            em.persist(ml312);
            Flight ml411 = new Flight("ML411");
            flightRoute = new FlightRoute(hongkong, narita);
            ml411.setFlightRoute(flightRoute);
            ml411.setAircraftConfig(aircraftConfig737tc);
            Flight ml412 = new Flight("ML412");
            returnFlightRoute = new FlightRoute(narita, hongkong);
            ml412.setFlightRoute(returnFlightRoute);
            ml412.setAircraftConfig(aircraftConfig737tc);
            ml411.setReturnFlight(ml412);
            em.persist(ml411);
            em.persist(ml412);
            
            Flight ml511 = new Flight("ML511");
            flightRoute = new FlightRoute(taoyuan, narita);
            ml511.setFlightRoute(flightRoute);
            ml511.setAircraftConfig(aircraftConfig737tc);
            Flight ml512 = new Flight("ML512");
            returnFlightRoute = new FlightRoute(narita, taoyuan);
            ml512.setFlightRoute(returnFlightRoute);
            ml512.setAircraftConfig(aircraftConfig737tc);
            ml511.setReturnFlight(ml512);
            em.persist(ml511);
            em.persist(ml512);
            
            Flight ml611 = new Flight("ML611");
            flightRoute = new FlightRoute(changi, sydney);
            ml611.setFlightRoute(flightRoute);
            ml611.setAircraftConfig(aircraftConfig737tc);
            Flight ml612 = new Flight("ML612");
            returnFlightRoute = new FlightRoute(hongkong, sydney);
            ml612.setFlightRoute(returnFlightRoute);
            ml612.setAircraftConfig(aircraftConfig737tc);
            ml611.setReturnFlight(ml612);
            em.persist(ml611);
            em.persist(ml612);
            Flight ml621 = new Flight("ML621");
            flightRoute = new FlightRoute(changi, sydney);
            ml621.setFlightRoute(flightRoute);
            ml621.setAircraftConfig(aircraftConfig737ae);
            Flight ml622 = new Flight("ML622");
            returnFlightRoute = new FlightRoute(hongkong, sydney);
            ml622.setFlightRoute(returnFlightRoute);
            ml622.setAircraftConfig(aircraftConfig737ae);
            ml621.setReturnFlight(ml622);
            em.persist(ml621);
            em.persist(ml622);
            Flight ml711 = new Flight("ML711");
            flightRoute = new FlightRoute(sydney, narita);
            ml711.setFlightRoute(flightRoute);
            ml711.setAircraftConfig(aircraftConfig747tc);
            Flight ml712 = new Flight("ML712");
            returnFlightRoute = new FlightRoute(narita, sydney);
            ml712.setFlightRoute(returnFlightRoute);
            ml712.setAircraftConfig(aircraftConfig747tc);
            ml711.setReturnFlight(ml712);
            em.persist(ml711);
            em.persist(ml712);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // FlightSchedulePlan
            FlightSchedulePlan flightSchedulePlan = new FlightSchedulePlan("ML711", RECURRENT_WEEK);
            flightSchedulePlan.setDayOfWeek(1);
            Date date = (Date) dateFormat.parse("2020-12-01");
            flightSchedulePlan.setStartDate(date);
            date = (Date) dateFormat.parse("2020-12-31");
            flightSchedulePlan.setEndDate(date);
            date = (Date) dateTimeFormat.parse("2020-12-07 09:00");
            FlightSchedule flightSchedule = new FlightSchedule(date, 14, 0);
            flightSchedulePlan.getFlightSchedules().add(flightSchedule);
            date = (Date) dateTimeFormat.parse("2020-12-14 09:00");
            flightSchedule = new FlightSchedule(date, 14, 0);
            flightSchedulePlan.getFlightSchedules().add(flightSchedule);
            date = (Date) dateTimeFormat.parse("2020-12-21 09:00");
            flightSchedule = new FlightSchedule(date, 14, 0);
            flightSchedulePlan.getFlightSchedules().add(flightSchedule);
            date = (Date) dateTimeFormat.parse("2020-12-28 09:00");
            flightSchedule = new FlightSchedule(date, 14, 0);
            flightSchedulePlan.getFlightSchedules().add(flightSchedule);
            
            
            
            
            flightSchedulePlan.getFlightSchedules().add(flightSchedule);
            
            
//ML711, Recurrent Weekly
//	
//	Monday, 9:00 AM, 1 Dec 20, 31 Dec 20, 14 Hours 0 Minute
//		ML712 Layover for 2 Hours
//	
//	F, F001, $6500
//	F, F002, $6000
//	J, J001, $3500
//	J, J002, $3000
//	Y, Y001, $1500
//	Y, Y002, $1000

            



            
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

}
