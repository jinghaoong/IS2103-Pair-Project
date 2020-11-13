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
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClass;
import static util.enumeration.CabinClass.*;
import util.enumeration.EmployeeRole;
import util.enumeration.PartnerRole;

/**
 *
 * @author yylow
 */
@Singleton
@LocalBean
@Startup
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
            
            
            // Partner
            Partner holidaydotcom = new Partner("Holiday.com", "holidaydotcom", password, PartnerRole.RESERVATION_MANAGER); // unsure
            em.persist(holidaydotcom);
            
            
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
            
            
            // Aircraft Type
            AircraftType b737 = new AircraftType("Boeing 737", 200);
            b737.getAircraftConfigurations().size();
            em.persist(b737);
            AircraftType b747 = new AircraftType("Boeing 747", 400);
            b747.getAircraftConfigurations().size();
            em.persist(b747);
            
            
            // Aircraft Configuration
            AircraftConfiguration aircraftConfig737ae = new AircraftConfiguration("Boeing 737 All Economy", 1, 180);
            aircraftConfig737ae.setAircraftType(b737);
            
            Integer[] arr = {3,3};
            CabinClassConfiguration ccc1 = new CabinClassConfiguration(Y, 1, 30, 6, arr);
            ccc1.getFares().size();
            ccc1.setAircraftConfig(aircraftConfig737ae);
            aircraftConfig737ae.getCabinClassConfigs().add(ccc1);
            
            em.persist(ccc1);
            em.persist(aircraftConfig737ae);
            b737.getAircraftConfigurations().add(aircraftConfig737ae);
            
            
            AircraftConfiguration aircraftConfig737tc = new AircraftConfiguration("Boeing 737 Three Classes", 3, 180);
            aircraftConfig737tc.setAircraftType(b737);
            
            arr = new Integer[] {1,1};
            CabinClassConfiguration ccc2 = new CabinClassConfiguration(CabinClass.F, 1, 5, 2, arr);
            ccc2.getFares().size();
            ccc2.setAircraftConfig(aircraftConfig737tc);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc2);
            
            arr = new Integer[] {2,2};
            CabinClassConfiguration ccc3 = new CabinClassConfiguration(CabinClass.J, 1, 5, 4, arr);
            ccc3.getFares().size();
            ccc3.setAircraftConfig(aircraftConfig737tc);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc3);
            
            arr = new Integer[] {3,3};
            CabinClassConfiguration ccc4 = new CabinClassConfiguration(CabinClass.Y, 1, 25, 6, arr);
            ccc4.getFares().size();
            ccc4.setAircraftConfig(aircraftConfig737tc);
            aircraftConfig737tc.getCabinClassConfigs().add(ccc4);
            
            em.persist(ccc2);
            em.persist(ccc3);
            em.persist(ccc4);
            em.persist(aircraftConfig737tc);
            b737.getAircraftConfigurations().add(aircraftConfig737tc);
            
            
            AircraftConfiguration aircraftConfig747ae = new AircraftConfiguration("Boeing 747 All Economy", 1, 380);
            aircraftConfig747ae.setAircraftType(b747);
            
            arr = new Integer[] {3,4,3};
            CabinClassConfiguration ccc5 = new CabinClassConfiguration(CabinClass.Y, 2, 38, 10, arr);
            ccc5.getFares().size();
            ccc5.setAircraftConfig(aircraftConfig747ae);
            aircraftConfig747ae.getCabinClassConfigs().add(ccc5);
            
            em.persist(ccc5);
            em.persist(aircraftConfig747ae);
            b747.getAircraftConfigurations().add(aircraftConfig747ae);
            
            
            AircraftConfiguration aircraftConfig747tc = new AircraftConfiguration("Boeing 747 Three Classes", 3, 360);
            aircraftConfig747tc.setAircraftType(b747);
            
            arr = new Integer[] {1,1};
            CabinClassConfiguration ccc6 = new CabinClassConfiguration(F, 1, 5, 2, arr);
            ccc6.getFares().size();
            ccc6.setAircraftConfig(aircraftConfig747tc);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc6);
            
            arr = new Integer[] {2,2,2};
            CabinClassConfiguration ccc7 = new CabinClassConfiguration(J, 2, 5, 6, arr);
            ccc7.getFares().size();
            ccc7.setAircraftConfig(aircraftConfig747tc);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc7);
            
            arr = new Integer[] {3,4,3};
            CabinClassConfiguration ccc8 = new CabinClassConfiguration(Y, 2, 32, 10, arr);
            ccc8.getFares().size();
            ccc8.setAircraftConfig(aircraftConfig747tc);
            aircraftConfig747tc.getCabinClassConfigs().add(ccc8);
            
            em.persist(ccc6);
            em.persist(ccc7);
            em.persist(ccc8);
            em.persist(aircraftConfig747tc);
            b747.getAircraftConfigurations().add(aircraftConfig747tc);
            
            
            // Flight Route
            FlightRoute sinHkg = new FlightRoute(changi, hongkong);
            FlightRoute hkgSin = new FlightRoute(hongkong, changi);
            sinHkg.setReturnFlightRoute(hkgSin);
            sinHkg.getFlights().size();
            hkgSin.getFlights().size();
            em.persist(sinHkg);
            em.persist(hkgSin);
            
            FlightRoute sinTpe = new FlightRoute(changi, taoyuan);
            FlightRoute tpeSin = new FlightRoute(taoyuan, changi);
            sinTpe.setReturnFlightRoute(tpeSin);
            sinTpe.getFlights().size();
            tpeSin.getFlights().size();
            em.persist(sinTpe);
            em.persist(tpeSin);
            
            FlightRoute sinNrt = new FlightRoute(changi, narita);
            FlightRoute nrtSin = new FlightRoute(narita, changi);
            sinNrt.setReturnFlightRoute(nrtSin);
            sinNrt.getFlights().size();
            nrtSin.getFlights().size();
            em.persist(sinNrt);
            em.persist(nrtSin);
            
            FlightRoute hkgNrt = new FlightRoute(hongkong, narita);
            FlightRoute nrtHkg = new FlightRoute(narita, hongkong);
            hkgNrt.setReturnFlightRoute(nrtHkg);
            hkgNrt.getFlights().size();
            nrtHkg.getFlights().size();
            em.persist(hkgNrt);
            em.persist(nrtHkg);
            
            FlightRoute tpeNrt = new FlightRoute(taoyuan, narita);
            FlightRoute nrtTpe = new FlightRoute(narita, taoyuan);
            tpeNrt.setReturnFlightRoute(nrtTpe);
            tpeNrt.getFlights().size();
            nrtTpe.getFlights().size();
            em.persist(tpeNrt);
            em.persist(nrtTpe);
            
            FlightRoute sinSyd = new FlightRoute(changi, sydney);
            FlightRoute sydSin = new FlightRoute(sydney, changi);
            sinSyd.setReturnFlightRoute(sydSin);
            sinSyd.getFlights().size();
            sydSin.getFlights().size();
            em.persist(sinSyd);
            em.persist(sydSin);
            
            FlightRoute sydNrt = new FlightRoute(sydney, narita);
            FlightRoute nrtSyd = new FlightRoute(narita, sydney);
            sydNrt.setReturnFlightRoute(nrtSyd);
            sydNrt.getFlights().size();
            nrtSyd.getFlights().size();
            em.persist(sydNrt);
            em.persist(nrtSyd);

            
            // Flights
            Flight ml111 = new Flight("ML111");
            ml111.setFlightRoute(sinHkg);
            sinHkg.getFlights().add(ml111);
            ml111.setAircraftConfig(aircraftConfig737tc);
            
            Flight ml112 = new Flight("ML112");
            ml112.setFlightRoute(hkgSin);
            hkgSin.getFlights().add(ml112);
            ml112.setAircraftConfig(aircraftConfig737tc);
            
            ml111.setReturnFlight(ml112);
            
            ml111.getFlightSchedules().size();
            ml112.getFlightSchedules().size();
            em.persist(ml111);
            em.persist(ml112);
            
            
            
            
            Flight ml211 = new Flight("ML211");
            ml211.setFlightRoute(sinTpe);
            sinTpe.getFlights().add(ml211);
            ml211.setAircraftConfig(aircraftConfig737tc);
            
            Flight ml212 = new Flight("ML212");
            ml212.setFlightRoute(tpeSin);
            tpeSin.getFlights().add(ml212);
            ml212.setAircraftConfig(aircraftConfig737tc);
            
            ml211.setReturnFlight(ml212);
            
            ml211.getFlightSchedules().size();
            ml212.getFlightSchedules().size();
            em.persist(ml211);
            em.persist(ml212);
            
            
            Flight ml311 = new Flight("ML311");
            ml311.setFlightRoute(sinNrt);
            sinNrt.getFlights().add(ml311);
            ml311.setAircraftConfig(aircraftConfig747tc);
            
            Flight ml312 = new Flight("ML312");
            ml312.setFlightRoute(nrtSin);
            nrtSin.getFlights().add(ml312);
            ml312.setAircraftConfig(aircraftConfig747tc);
            
            ml311.setReturnFlight(ml312);
            
            ml311.getFlightSchedules().size();
            ml312.getFlightSchedules().size();
            em.persist(ml311);
            em.persist(ml312);
            
            
            Flight ml411 = new Flight("ML411");
            ml411.setFlightRoute(hkgNrt);
            hkgNrt.getFlights().add(ml411);
            ml411.setAircraftConfig(aircraftConfig737tc);
            
            Flight ml412 = new Flight("ML412");
            ml412.setFlightRoute(nrtHkg);
            nrtHkg.getFlights().add(ml412);
            ml412.setAircraftConfig(aircraftConfig737tc);
            
            ml411.setReturnFlight(ml412);
            
            ml411.getFlightSchedules().size();
            ml412.getFlightSchedules().size();
            em.persist(ml411);
            em.persist(ml412);
            
            
            Flight ml511 = new Flight("ML511");
            ml511.setFlightRoute(tpeNrt);
            tpeNrt.getFlights().add(ml511);
            ml511.setAircraftConfig(aircraftConfig737tc);
            
            Flight ml512 = new Flight("ML512");
            ml512.setFlightRoute(nrtTpe);
            nrtTpe.getFlights().add(ml512);
            ml512.setAircraftConfig(aircraftConfig737tc);
            
            ml511.setReturnFlight(ml512);
            
            ml511.getFlightSchedules().size();
            ml512.getFlightSchedules().size();
            em.persist(ml511);
            em.persist(ml512);
            
            
            Flight ml611 = new Flight("ML611");
            ml611.setFlightRoute(sinSyd);
            sinSyd.getFlights().add(ml611);
            ml611.setAircraftConfig(aircraftConfig737tc);
            
            Flight ml612 = new Flight("ML612");
            ml612.setFlightRoute(sydSin);
            sydSin.getFlights().add(ml612);
            ml612.setAircraftConfig(aircraftConfig737tc);
            
            ml611.setReturnFlight(ml612);
            
            ml611.getFlightSchedules().size();
            ml612.getFlightSchedules().size();
            em.persist(ml611);
            em.persist(ml612);
            
            
            Flight ml621 = new Flight("ML621");
            ml621.setFlightRoute(sinSyd);
            sinSyd.getFlights().add(ml621);
            ml621.setAircraftConfig(aircraftConfig737ae);
            
            Flight ml622 = new Flight("ML622");
            ml622.setFlightRoute(sydSin);
            sydSin.getFlights().add(ml622);
            ml622.setAircraftConfig(aircraftConfig737ae);
            
            ml621.setReturnFlight(ml622);
            
            ml621.getFlightSchedules().size();
            ml622.getFlightSchedules().size();
            em.persist(ml621);
            em.persist(ml622);
            
            
            Flight ml711 = new Flight("ML711");
            ml711.setFlightRoute(sydNrt);
            sydNrt.getFlights().add(ml711);
            ml711.setAircraftConfig(aircraftConfig747tc);
            
            Flight ml712 = new Flight("ML712");
            ml712.setFlightRoute(nrtSyd);
            nrtSyd.getFlights().add(ml712);
            ml712.setAircraftConfig(aircraftConfig747tc);
            
            ml711.setReturnFlight(ml712);
            
            ml711.getFlightSchedules().size();
            ml712.getFlightSchedules().size();
            em.persist(ml711);
            em.persist(ml712);
            
            /*
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
            */
            
            
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
