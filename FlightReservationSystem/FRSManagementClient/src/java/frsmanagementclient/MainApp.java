/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightOperationSessionBeanRemote;
import ejb.session.stateless.FlightPlanningSessionBeanRemote;
import ejb.session.stateless.ValidatorSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClass;
import util.enumeration.EmployeeRole;
import util.enumeration.FlightScheduleType;
import util.exception.AircraftConfigExistException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.FareBasisCodeExistException;
import util.exception.FlightAlreadyExistException;
import util.exception.FlightNumberDisabledException;
import util.exception.FlightRouteAlreadyExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleOverlapException;
import util.exception.InvalidDateTimeException;
import util.exception.InvalidDurationException;
import util.exception.MaximumSeatCapacityExceededException;
import util.exception.NumberOfSeatsPerRowMismatchException;
import util.exception.ViolationException;

/**
 *
 * @author jinghao
 */
public class MainApp {
    
    private Scanner scanner;
    private Employee employee;
    
    //private Integer flightNumberIncrement = 5;
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote;
    private FlightOperationSessionBeanRemote flightOperationSessionBeanRemote;
    private ValidatorSessionBeanRemote validatorSessionBeanRemote;
    
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote, FlightOperationSessionBeanRemote flightOperationSessionBeanRemote, ValidatorSessionBeanRemote validatorSessionBeanRemote) {
        
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightPlanningSessionBeanRemote = flightPlanningSessionBeanRemote;
        this.flightOperationSessionBeanRemote = flightOperationSessionBeanRemote;
        this.validatorSessionBeanRemote = validatorSessionBeanRemote;
        
        /*
        if(this.employeeSessionBeanRemote == null)
            System.out.println("********** IS NULL");
        else
            System.out.println("********** NOT null");
        */
    }
    
    public void runApp() {
        employee = null;
        scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("***** Welcome to the FRS Management Client *****\n");
            System.out.println("1: Employee Login");
            System.out.println("- Input any other integer to exit -\n");

            System.out.print("> ");
            if (scanner.hasNextInt()) {
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                
                if (response == 1) {
                    try {
                        response = 0;
                        doLogin();
                        EmployeeRole employeeRole = employee.getEmployeeRole();
                        
                        if (employeeRole == EmployeeRole.SYSTEM_ADMIN) {
                            adminMenu();
                        }
                        else if (employeeRole == EmployeeRole.FLEET_MANAGER) {
                            fleetManagerMenu();
                        }
                        else if (employeeRole == EmployeeRole.ROUTE_PLANNER) {
                            routePlannerMenu();
                        }
                        else if(employeeRole == EmployeeRole.SCHEDULE_MANAGER) {
                            scheduleManagerMenu();
                        }
                        else { // employeeRole == EmployeeRole.SALES_MANAGER
                            salesManagerMenu();
                        }
                    }
                    catch (EmployeeNotFoundException ex) {
                        System.out.println("Employee not found, login unsuccessful ...\n");
                    }

                } else {
                    System.out.println("Leaving FRS Management Client ...");
                    break;
                }
            } else {
                System.out.println("Input integers only!\n");
                scanner.nextLine();
            }

        }
        
    }
    
    private void doLogin() throws EmployeeNotFoundException {
        
        System.out.print("Enter Employee username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Employee password: ");
        String password = scanner.nextLine().trim();
        System.out.println();
        
        try {
            employee = employeeSessionBeanRemote.employeeLogin(username, password);
            System.out.println("Employee login successful!\n");
        }
        catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException(ex.getMessage());
        }
    }
    
    private void doLogout() {
        employee = null;
        System.out.println("Logging out ...\n");
    }
    
    private void adminMenu() {
        String response = "";
        
        while (true) {
            System.out.println("***** FRS Management - System Admin Panel");
            System.out.println("A1: Create Aircraft Configuration");
            System.out.println("A2: View All Aircraft Configurations");
            System.out.println("A3: View Aircraft Configuration Details");
            
            
            System.out.println("B1: Create Flight Route");
            System.out.println("B2: View All Flight Routes");
            System.out.println("B3: Delete Flight Route");
            
            
            System.out.println("C1: Create Flight");
            System.out.println("C2: View All Flights");
            System.out.println("C3: View Flight Details"); // includes Update, Delete
            
            System.out.println("D1: Create Flight Schedule Plan");
            System.out.println("D2: View All Flight Schedule Plans");
            System.out.println("D3: View Flight Schedule Plan Details"); // includes Update, Delete
            
            /*
            sales manager methods tbd
            */
            
            System.out.println("Q: Logout\n");
            response = "";
            
            while (true) {
                System.out.print("> ");
                response = scanner.next();
                
                if (response.equalsIgnoreCase("A1")) {
                    try {
                        doCreateAircraftConfig();
                    }
                    catch (MaximumSeatCapacityExceededException | NumberOfSeatsPerRowMismatchException ex) {
                        System.out.println("\nFollowing error occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response.equalsIgnoreCase("A2")) {
                    doViewAllFlights();
                }
                else if (response.equalsIgnoreCase("A3")) {
                    doViewFlightDetails();
                }
                
                else if (response.equalsIgnoreCase("B1")) {
                    doCreateFlightRoute();
                }
                else if (response.equalsIgnoreCase("B2")) {
                    doViewAllFlightRoutes();
                }
                else if (response.equalsIgnoreCase("B3")) {
                    doDeleteFlightRoute();
                }
                
                else if (response.equalsIgnoreCase("C1")) {
                    doCreateFlight();
                }
                else if (response.equalsIgnoreCase("C2")) {
                    doViewAllFlights();
                }
                else if (response.equalsIgnoreCase("C3")) {
                    doViewFlightDetails();
                }
                
                else if (response.equalsIgnoreCase("D1")) {
                    doCreateFlightSchedulePlan();
                }
                else if (response.equalsIgnoreCase("D2")) {
                    doViewAllFlightSchedulePlans();
                }
                else if (response.equalsIgnoreCase("D3")) {
                    doViewFlightSchedulePlanDetails();
                }
                
                
                else if (response.equalsIgnoreCase("Q")) {
                    doLogout();
                    System.out.println("Logging out ...\n");
                    break;
                }
                else {
                    System.out.println("Invalid response!\n");
                }
                
            }
        }
        
    }
    
    private void fleetManagerMenu() {
        Integer response = 0;
        
        while (true) {
            System.out.println("***** FRS Management - Flight Planning *****");
            System.out.println("Welcome, Fleet Manager " + employee.getName() + "!\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configurations");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while (response < 1 || response > 4) {
                
                System.out.print("> ");
                if (scanner.hasNextInt()) {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                }
                else {
                    response = 0;
                    scanner.nextLine();
                }
                
                if (response == 1) {
                    try {
                        doCreateAircraftConfig();
                    }
                    catch (MaximumSeatCapacityExceededException | NumberOfSeatsPerRowMismatchException ex) {
                        System.out.println("\nFollowing error occurred: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2) {
                    doViewAllAircraftConfigs();
                }
                else if (response == 3) {
                    doViewAircraftConfigDetails();
                }
                else if (response == 4) {
                    doLogout();
                    break;
                }
                else {
                    System.out.println("Please input an integer from 1 to 4\n");
                    break;
                }
            }
            
            if (response == 4) {
                break;
            }
        }
    }
    
    private void routePlannerMenu() {
        Integer response = 0;
        
        while (true) {
            System.out.println("***** FRS Management - Flight Planning *****");
            System.out.println("Welcome, Route Planner " + employee.getName() + "!\n");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Logout\n");
            response = 0;
            
            while (response < 1 || response > 4) {
                
                System.out.print("> ");
                if (scanner.hasNextInt()) {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                }
                else {
                    response = 0;
                    scanner.nextLine();
                }
                
                
                if (response == 1) {
                    doCreateFlightRoute();
                }
                else if (response == 2) {
                    doViewAllFlightRoutes();
                }
                else if (response == 3) {
                    doDeleteFlightRoute();
                }
                else if (response == 4) {
                    doLogout();
                    break;
                }
                else {
                    System.out.println("Please input an integer from 1 to 4\n");
                    break;
                }
            }
            
            if (response == 4) {
                break;
            }
        }
    }
    
    private void scheduleManagerMenu() {
        Integer response = 0;
        
        while (true) {
            System.out.println("***** FRS Management - Flight Operation *****");
            System.out.println("Welcome, Schedule Manager " + employee.getName() + "!\n");
            
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details"); // includes Update, Delete
            
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Schedule Plans");
            System.out.println("6: View Flight Schedule Plan Details"); // includes Update, Delete
            
            System.out.println("7: Logout\n");
            response = 0;
            
            while (response < 1 || response > 7) {
                
                System.out.print("> ");
                if (scanner.hasNextInt()) {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                }
                else {
                    response = 0;
                    scanner.nextLine();
                }
                
                
                if (response == 1) {
                    doCreateFlight();
                }
                else if (response == 2) {
                    doViewAllFlights();
                }
                else if (response == 3) {
                    doViewFlightDetails(); // includes Update, Delete
                }
                else if (response == 4) {
                    doCreateFlightSchedulePlan();
                }
                else if (response == 5) {
                    doViewAllFlightSchedulePlans();
                }
                else if (response == 6) {
                    doViewFlightSchedulePlanDetails(); // includes Update, Delete
                }
                else if (response == 7) {
                    doLogout();
                    break;
                }
                else {
                    System.out.println("Please input an integer from 1 to 7\n");
                    break;
                }
            }
            
            if (response == 7) {
                break;
            }
        }
    }
    
    private void salesManagerMenu() { 
        Integer response = 0;
        
         while (true) {
            System.out.println("***** FRS Management - Sales Management *****");
            System.out.println("Welcome, Sales Manager " + employee.getName() + "!\n");
            
            System.out.println("1: View Seats Inventory");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: Logout\n");
            response = 0;
            
            while (response < 1 || response > 3) {
                
                System.out.print("> ");
                if (scanner.hasNextInt()) {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();
                }
                else {
                    response = 0;
                    scanner.nextLine();
                }
                
                
                if (response == 1) {
                    doViewSeatsInventory();
                }
                else if (response == 2) {
                    doViewFlightReservations();
                }
                else if (response == 3) {
                    doLogout();
                    System.out.println("Logging out ...");
                    break;
                }
                else {
                    System.out.println("Please input an integer from 1 to 3\n");
                    break;
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    /*
    Fleet Manager methods
    */
    private void doCreateAircraftConfig() throws MaximumSeatCapacityExceededException, NumberOfSeatsPerRowMismatchException {
        Integer response = 0;
        List<AircraftType> aircraftTypes = flightPlanningSessionBeanRemote.retrieveAllAircraftTypes();
        AircraftType aircraftType = null;
        
        while (true) {
            System.out.println("*** Create Aircraft Configuration ***\n");
            System.out.println("Select Aircraft Type (0 : skip , 1 : select, other integer : exit)");
            response = 0;
            for (Integer i = 0; i < aircraftTypes.size(); i++) {
                System.out.print("Aircraft Type - " + aircraftTypes.get(i).getAircraftTypeName() + ": ");
                response = scanner.nextInt();
                
                if (response == 0) {
                    //skip
                }
                else if (response == 1) {
                    aircraftType = aircraftTypes.get(i);
                    scanner.nextLine();
                    break;
                }
                else {
                    break;
                }
            }
            
            if (response == 1) {
                break;
            } else {
                System.out.println("... Press enter to return ...");
                scanner.nextLine();
                System.out.println();
                return;
            }
        }
        
        System.out.print("Enter Aircraft Configuration Name: "); // unique
        String name = scanner.nextLine().trim();
        System.out.print("Enter number of Cabin Classes (1 - 4): "); // 1 - 4
        Integer numCabinClass = scanner.nextInt();
        scanner.nextLine();
        
        List<CabinClassConfiguration> cabinClassConfigs = new ArrayList<>();
        Integer totalSeatCapacity = 0;
        
        for (Integer j = 0; j < numCabinClass; j++) {
            System.out.println("Cabin Class " + (j+1) + " Configuration");
            System.out.print("Cabin Class (First : F, Business : J, Premium Economy : W, Economy : Y): ");
            CabinClass cabinClass = CabinClass.valueOf(scanner.next().trim());
            System.out.print("Number of aisles: "); // 0 - 2
            Integer numAisles = scanner.nextInt();
            System.out.print("Number of seat rows: ");
            Integer numSeatRows = scanner.nextInt();
            System.out.print("Number of seats abreast: ");
            Integer numSeatsAbreast = scanner.nextInt();
            System.out.println("Actual seating configuration");
            Integer[] seatingConfig = new Integer[numAisles+1];
            Integer seatCheck = 0;

            for (Integer x = 1; x <= numAisles + 1; x++) {
                System.out.print("Number of seats in section " + x + ": ");
                Integer num = scanner.nextInt();
                seatCheck += num;
                seatingConfig[x-1] = num;
            }
            
            if (numSeatsAbreast != seatCheck) {
                throw new NumberOfSeatsPerRowMismatchException("Number of seats abreast does not match seating config!");
            }
            
            totalSeatCapacity += numSeatRows * numSeatsAbreast;
            
            CabinClassConfiguration cabinClassConfig = new CabinClassConfiguration(cabinClass, numAisles, numSeatRows, numSeatsAbreast, seatingConfig);
            cabinClassConfigs.add(cabinClassConfig);
            System.out.println();
        }
        
        if (totalSeatCapacity > aircraftType.getMaximumPassengerCapacity()) {
                throw new MaximumSeatCapacityExceededException("Aircraft Type Maximum Seat Capacity Exceeded!");
        }
        
        AircraftConfiguration aircraftConfig = new AircraftConfiguration(name, numCabinClass, totalSeatCapacity);
        aircraftConfig.setCabinClassConfigs(cabinClassConfigs);
        aircraftConfig.setAircraftType(aircraftType);
        try {
            validatorSessionBeanRemote.validate(aircraftConfig);
            flightPlanningSessionBeanRemote.createAircraftConfig(aircraftConfig);
            System.out.println("Aircraft config '" + aircraftConfig.getAircraftConfigName() + "' successfully created!\n");
        }
        catch (AircraftConfigExistException | ViolationException ex) {
            System.out.println("\nFollowing error(s) occurred: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllAircraftConfigs() {
        List<AircraftConfiguration> aircraftConfigs = flightPlanningSessionBeanRemote.retrieveAllAircraftConfigs();
        System.out.println("*** List of Aircraft Configuration Records ***\n");
        
        for (AircraftConfiguration ac : aircraftConfigs) {
            System.out.println(ac.getAircraftConfigName());
        }
        System.out.println();
        
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doViewAircraftConfigDetails() {
        Integer response = 0;
        List<AircraftConfiguration> aircraftConfigs = flightPlanningSessionBeanRemote.retrieveAllAircraftConfigs();
        
        System.out.println("*** View Aircraft Configuration Details ***\n");
        
            System.out.println("Select Aircraft Configuration to view (0 : skip, 1 : select, other integer : return)");
            
            for (AircraftConfiguration ac : aircraftConfigs) {
                System.out.print(ac.getAircraftConfigName() + ": ");
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                
                if (response == 1) {
                    System.out.println("-----------------------------------------------------------");
                    System.out.println("Aircraft Configuration Name : " + ac.getAircraftConfigName());
                    System.out.println("Number of Cabin Classes : " + ac.getNumberOfCabinClass());
                    System.out.println("Maximum Seat Capacity: " + ac.getMaximumSeatCapacity());
                    System.out.println();
                    
                    for (Integer i = 1; i <= ac.getNumberOfCabinClass(); i++) {
                        System.out.println("- Cabin Class " + ac.getCabinClassConfigs().get(i-1).getCabinClass() + " Details -");
                        System.out.println("Maximum Seat Capacity: " + ac.getCabinClassConfigs().get(i-1).getMaximumCapacity());
                        System.out.println("Number of Aisles: " + ac.getCabinClassConfigs().get(i-1).getNumberOfAisles());
                        System.out.println("Number of Rows : " + ac.getCabinClassConfigs().get(i-1).getNumberOfRows());
                        System.out.println("Number of Seats Abreast: " + ac.getCabinClassConfigs().get(i-1).getNumberOfSeatsAbreast());
                        
                        String seatConfig = "";
                        
                        for (Integer seats : ac.getCabinClassConfigs().get(i-1).getSeatingConfig()) {
                            seatConfig += seats + "-";
                        }
                        seatConfig = seatConfig.substring(0, seatConfig.length()-1);
                        
                        System.out.println("Seat Configuration: " + seatConfig);
                        
                        System.out.println();
                    }
                    break;
                }
                if (response > 1) {
                    break;
                }
            }
            System.out.println();
            System.out.println("... Press enter to continue ...");
            scanner.nextLine();
            System.out.println();
    }
    
    /*
    Route Planner methods
    */
    private void doCreateFlightRoute() {
        Integer response = 0;
        
        System.out.println("*** Create Flight Route ***\n");

        System.out.println("Enter Origin & Destination Airports");
        System.out.print("Origin Airport IATA Code: ");
        String originCode = scanner.nextLine().trim();
        System.out.print("Destination Airport IATA Code: ");
        String destinationCode = scanner.nextLine().trim();
        System.out.println();
        
        try {
            Airport originAirport = flightPlanningSessionBeanRemote.retrieveAirportByCode(originCode);
            Airport destinationAirport = flightPlanningSessionBeanRemote.retrieveAirportByCode(destinationCode);
            
            FlightRoute flightRoute = flightPlanningSessionBeanRemote.createFlightRoute(originAirport, destinationAirport);
            
            System.out.println("Flight Route " + originCode + "-" + destinationCode + " successfully created!\n");
            
            System.out.print("Create return Flight Route?\n(0 : No, 1: Yes) : ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            
            if (response == 0) {
                // do nothing
            }
            else if (response == 1) {
                Airport returnOriginAirport = destinationAirport;
                Airport returnDestinationAirport = originAirport;
                
                FlightRoute returnFlightRoute = flightPlanningSessionBeanRemote.createFlightRoute(returnOriginAirport, returnDestinationAirport);
                flightRoute.setReturnFlightRoute(returnFlightRoute);
                flightPlanningSessionBeanRemote.updateFlightRoute(flightRoute);
                
                System.out.println("Return Flight Route " + returnOriginAirport.getAirportCode() + "-" + returnDestinationAirport.getAirportCode() + " successfully created!\n");
            }
            
            System.out.println("... Press enter to continue ...");
            scanner.nextLine();
        }
        catch (AirportNotFoundException | FlightRouteAlreadyExistException | ViolationException ex) {
            System.out.println("\nFollowing error(s) occurred: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = flightPlanningSessionBeanRemote.retrieveAllFlightRoutes();
        
        System.out.println("*** List of Flight Routes ***\n");
        
        for (FlightRoute fr : flightRoutes) {
            if (fr.getEnabled() == true) { // check whether flight route is disabled
                System.out.println("Flight Route: " + fr.getOriginAirport().getAirportName() + " -> " + fr.getDestinationAirport().getAirportName() + " (" + fr.getOriginAirport().getAirportCode() + "-" + fr.getDestinationAirport().getAirportCode() + ")");
                if (fr.getReturnFlightRoute() != null) {
                    System.out.println("\tReturn: " + fr.getDestinationAirport().getAirportName() + " -> " + fr.getOriginAirport().getAirportName() + " (" + fr.getDestinationAirport().getAirportCode() + "-" + fr.getOriginAirport().getAirportCode() + ")");
                }
                System.out.println();
            }
            //System.out.println();
        }
        
        System.out.println();
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doDeleteFlightRoute() {
        System.out.println("*** Delete Flight Route ***");
        
        System.out.println("Enter Origin and Destination Airport Codes to find Flight Route for deletion");
        System.out.print("Origin Airport Code: ");
        String originAirportCode = scanner.nextLine();
        System.out.print("Destination Airport Code: ");
        String destinationAirportCode = scanner.nextLine();
        System.out.println();
        
        try {
            FlightRoute flightRoute = flightPlanningSessionBeanRemote.retrieveFlightRouteByCodes(originAirportCode, destinationAirportCode);
            System.out.println(flightPlanningSessionBeanRemote.deleteFlightRouteById(flightRoute.getFlightRouteId()));
        }
        catch (FlightRouteNotFoundException ex) {
            System.out.println("Following error occurred: " + ex.getMessage());
        }
        
        System.out.println();
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    /*
    Schedule Manager methods
    */
    
    private void doCreateFlight() {
        Integer response = 0;
        /*
        String largestFlightNumber = flightOperationSessionBeanRemote.retrieveLargestFlightNumber();
        Integer largestFlightDigits = Integer.parseInt(largestFlightNumber.split("ML")[1]);
        Integer number = largestFlightDigits + flightNumberIncrement;
        */
        
        System.out.println("*** Create Flight ***\n");
        
        System.out.print("Enter Flight Number Digits: ");
        Integer digits = scanner.nextInt();
        String flightNumber = "ML" + digits;
        scanner.nextLine();
        //String flightNumber = "ML" + number;
        
        List<FlightRoute> flightRoutes = flightPlanningSessionBeanRemote.retrieveAllFlightRoutes();
        FlightRoute flightRoute = null;
        
        if (flightRoutes.isEmpty()) {
            System.out.println();
            System.out.println("... No Flight Routes available, press enter to return ...");
            scanner.nextLine();
            System.out.println();
            return;
        }
        
        while (true) {
            System.out.println("Select a Flight Route (0 : skip, 1 : select)");
            
            for (FlightRoute fr : flightRoutes) {
                System.out.print(fr.getOriginAirport().getAirportCode() + "-" + fr.getDestinationAirport().getAirportCode() + ": ");
                response = scanner.nextInt();
                scanner.nextLine();
                
                if (response == 1) {
                    System.out.println("Flight Route " + fr.getOriginAirport().getAirportCode() + "-" + fr.getDestinationAirport().getAirportCode() + " selected!\n");
                    flightRoute = fr;
                    break;
                }
            }
            if (response == 1) {
                break;
            }
            else {
                System.out.println();
                System.out.println("... No Flight Route Selected, press enter to return ...");
                scanner.nextLine();
                System.out.println();
                return;
            }
        }
        
        
        response = 0;
        List<AircraftConfiguration> aircraftConfigs = flightPlanningSessionBeanRemote.retrieveAllAircraftConfigs();
        AircraftConfiguration aircraftConfig = null;
        
        if (aircraftConfigs.isEmpty()) {
            System.out.println();
            System.out.println("... No Aircraft Configurations available, press enter to return ...");
            scanner.nextLine();
            System.out.println();
            return;
        }
        
        while (true) {
            System.out.println("Select an Aircraft Configuration (0 : skip, 1 : select)");
            for (AircraftConfiguration ac : aircraftConfigs) {
                System.out.print(ac.getAircraftConfigName() + ": ");
                response = scanner.nextInt();
                scanner.nextLine();
                
                if (response == 1) {
                    System.out.println("'" + ac.getAircraftConfigName() + "' selected!\n");
                    aircraftConfig = ac;
                    break;
                }
            }
            if (response == 1) {
                break;
            }
            else {
                System.out.println();
                System.out.println("... No Aircraft Configuration Selected, press enter to return ...");
                scanner.nextLine();
                System.out.println();
            return;
            }
        }
        
        response = 0;
        
        try {
            Flight newFlight = new Flight(flightNumber);
            newFlight.setFlightRoute(flightRoute);
            newFlight.setAircraftConfig(aircraftConfig);
            
            validatorSessionBeanRemote.validate(newFlight);
            newFlight = flightOperationSessionBeanRemote.createFlight(newFlight);
            
            System.out.println("Flight " + newFlight.getFlightNumber() + " successfully created!");
            
            if (flightRoute.getReturnFlightRoute() != null) {
                System.out.println("Selected Flight Route has existing Return Flight Route.");
                System.out.print("Create complementary return flight? (0 : No, 1 : Yes):  ");
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                if (response == 1) {
                    FlightRoute returnFlightRoute = flightRoute.getReturnFlightRoute();
                    String returnFlightNumber = "ML" + (digits+1);
                    //String returnFlightNumber = "ML" + (number+1);

                    Flight newReturnFlight = new Flight(returnFlightNumber);

                    newReturnFlight.setFlightRoute(returnFlightRoute);
                    newReturnFlight.setAircraftConfig(aircraftConfig);
                    newReturnFlight = flightOperationSessionBeanRemote.createFlight(newReturnFlight);

                    newFlight.setReturnFlight(newReturnFlight);
                    flightOperationSessionBeanRemote.updateFlight(newFlight);

                    System.out.println("Return Flight " + newReturnFlight.getFlightNumber()+ " successfully created!");
                }
            }
        }
        catch (FlightAlreadyExistException | ViolationException ex) {
            System.out.println("\nFollowing error(s) occurred: " + ex.getMessage() + "\n");
        }
        
        System.out.println();
        System.out.println("... Press enter to return ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doViewAllFlights() {
        List<Flight> flights = flightOperationSessionBeanRemote.retrieveAllFlights();
        
        System.out.println("*** List of Flights ***\n");
        
        for (Flight f : flights) {
            if (f.getEnabled() == true) { // check whether flight is disabled
                System.out.println(f.getFlightNumber() + " " + f.getFlightRoute().getOriginAirport().getAirportCode() + "-" + f.getFlightRoute().getDestinationAirport().getAirportCode() + " (" + f.getAircraftConfig().getAircraftConfigName() + ")");
                if (f.getReturnFlight() != null) {
                    /*
                    Flight rf = f.getReturnFlight();
                    System.out.println("\tReturn: " + rf.getFlightNumber() + " " + rf.getFlightRoute().getOriginAirport().getAirportCode() + "-" + rf.getFlightRoute().getDestinationAirport().getAirportCode());
                    */
                    
                    System.out.print("\t");
                } else {
                    System.out.println();
                }
            }
        }
        System.out.println();
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
                
    private void doViewFlightDetails() { // includes Update, Delete
        Integer response = 0;
        List<Flight> flights = flightOperationSessionBeanRemote.retrieveAllFlights();

        System.out.println("*** View Flight Details ***\n");
        
        if (flights.isEmpty()) {
            System.out.println("... No Flights available, press enter to return ...");
            scanner.nextLine();
            return;
        }
        
        System.out.println("Select Flight to view (0 : skip, 1 : select, other integer : return)");
        for (Flight f : flights) {
            System.out.print("Flight " + f.getFlightNumber() + ": ");
            response = scanner.nextInt();
            scanner.nextLine();
            
            if (response == 0) {
                //skip
            }
            else if (response == 1) {
                System.out.println();
                System.out.println("*** Flight Details (" + f.getFlightNumber() + ") ***\n");
                
                System.out.println("Origin-Destination Pair: " + f.getFlightRoute().getOriginAirport().getAirportCode() + "-" + f.getFlightRoute().getDestinationAirport().getAirportCode());
                System.out.println("Aircraft Configuration: " + f.getAircraftConfig().getAircraftConfigName());
                System.out.println();
                
                List<CabinClassConfiguration> cabinClasses = f.getAircraftConfig().getCabinClassConfigs();
                
                for (int i = 0; i < cabinClasses.size(); i++) {
                    CabinClassConfiguration cc = cabinClasses.get(i);
                    System.out.println("Cabin Class " + (i+1) + " (" + cc.getCabinClass() + ")");
                    System.out.println("Number of seats: " + cc.getNumSeats());
                    System.out.println();
                }
                
                if (f.getFlightSchedules().isEmpty()) {
                    System.out.println("... No available Flight Schedules ...");
                }
                else {
                    System.out.println("-Available Flight Schedules-");
                    for (FlightSchedule fs : f.getFlightSchedules()) {
                        System.out.println(fs.getArrivalDateTime());
                    }
                }
                
                System.out.println();
                response = 0;
                
                while (response < 1 || response > 3) {
                    System.out.println("1: Update Flight");
                    System.out.println("2: Delete Flight");
                    System.out.println("Other integer : Exit Flight Details\n");
                    
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (response == 1) {
                        doUpdateFlight(f);
                    }
                    else if (response == 2) {
                        doDeleteFlight(f);
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                break;
            }
        }
        
        System.out.println();
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doUpdateFlight(Flight flight) {
        Integer response = 0;
        System.out.println("*** Update Flight Details ***\n");
        
        if (flight.getFlightSchedules().isEmpty()) {
            List<AircraftConfiguration> aircraftConfigs = flightPlanningSessionBeanRemote.retrieveAllAircraftConfigs();
            System.out.println("Choose Aircraft Configuration (0 : skip, 1 : select, other integer : exit");
            
            for (AircraftConfiguration ac : aircraftConfigs) {
                System.out.print(ac.getAircraftConfigName() + ": ");
                response = scanner.nextInt();
                scanner.nextLine();
                if (response == 0) {
                    //skip
                }
                else if (response == 1) {
                    flight.setAircraftConfig(ac);
                    flightOperationSessionBeanRemote.updateFlight(flight);
                    System.out.println("Aircraft Configuration successfully updated to '" + ac.getAircraftConfigName() + "'!");
                    break;
                }
                else {
                    break;
                }
            }
        }
        else {
            System.out.println("Flight is in use, unable to update Flight!\n");
        }
        System.out.println();
        System.out.print("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doDeleteFlight(Flight flight) {
        Integer response = 0;
        System.out.println("*** Update Flight Details ***\n");
        
        System.out.println("1 : Confirm Deletion");
        System.out.println("... Enter any other integer to cancel deletion ...\n");
        response = scanner.nextInt();
        
        if (response == 1) {
            System.out.println(flightOperationSessionBeanRemote.deleteFlight(flight) + "\n");
        } else {
            System.out.println("Returning to previous menu ...\n");
        }
    }
    
    private void doCreateFlightSchedulePlan() { // Single, Multiple, Recurrent (n day / weekly)
        Integer response = 0;
        Flight flight = null;
        List<FlightSchedule> flightSchedules = new ArrayList<>();
        FlightSchedulePlan flightSchedulePlan = new FlightSchedulePlan();
        
        while (response < 1 || response > 4) {
            System.out.println("*** Create Flight Schedule Plan ***\n");
            
            System.out.println("Select Type of Flight Schedule Plan");
            System.out.println("1: Single schedule");
            System.out.println("2: Multiple schedules");
            System.out.println("3: Recurrent schedules every n day");
            System.out.println("4: Recurrent schedules every week");
            System.out.println("5: Return to menu");
            
            System.out.print("> ");
            response = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            
            if (response >= 1 && response <= 4) {
                try {
                    System.out.print("Enter Flight Number: ");
                    String flightNumber = scanner.nextLine().trim();
                    
                    flight = flightOperationSessionBeanRemote.retrieveFlightByNumber(flightNumber);
                    flightSchedulePlan.setFlightNumber(flightNumber);
                    
                    if (response == 1) { // Single
                        flightSchedulePlan.setFlightScheduleType(FlightScheduleType.SINGLE);
                        FlightSchedule flightSchedule = doCreateFlightSchedule(flight);
                        flightSchedules.add(flightSchedule);
                    }
                    else if (response == 2) { // Multiple
                        flightSchedulePlan.setFlightScheduleType(FlightScheduleType.MULTIPLE);
                        System.out.print("Number of Flight Schedules: ");
                        Integer num = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println();
                        
                        for (int i = 0; i < num; i++) {
                            flightSchedules.add(doCreateFlightSchedule(flight));
                        }
                        
                        flightSchedules.sort((FlightSchedule f1, FlightSchedule f2) -> f1.getDepartureDateTime().compareTo(f2.getDepartureDateTime()));
                    }
                    else if (response == 3) { // Recurrent n day
                        flightSchedulePlan.setFlightScheduleType(FlightScheduleType.RECURRENT_DAY);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        
                        System.out.print("Enter n day: ");
                        Integer nDay = scanner.nextInt();
                        flightSchedulePlan.setnDay(nDay);
                        scanner.nextLine();
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        try {
                            Date date = dateFormat.parse(scanner.nextLine().trim());
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(date);
                            temp.add(DATE, 1);
                            flightSchedulePlan.setEndDate(temp.getTime());
                        }
                        catch (ParseException ex) {
                            throw new InvalidDateTimeException("Invalid end date format entered!");
                        }
                        
                        FlightSchedule currFlightSchedule = doCreateFlightSchedule(flight);
                        flightSchedules.add(currFlightSchedule);
                        
                        Calendar currDateTime = Calendar.getInstance();
                        currDateTime.setTime(currFlightSchedule.getArrivalDateTime());
                        currDateTime.add(DAY_OF_MONTH, nDay);
                        
                        while (currDateTime.getTime().before(flightSchedulePlan.getEndDate())) {
                            Calendar departureDateTime = Calendar.getInstance();
                            departureDateTime.setTime(currFlightSchedule.getDepartureDateTime());
                            departureDateTime.add(DAY_OF_MONTH, nDay);
                            
                            FlightSchedule nextFlightSchedule = new FlightSchedule();
                            
                            nextFlightSchedule.setFlight(flight);
                            nextFlightSchedule.setDepartureDateTime(departureDateTime.getTime());
                            nextFlightSchedule.setEstimatedFlightDurationHour(currFlightSchedule.getEstimatedFlightDurationHour());
                            nextFlightSchedule.setEstimatedFlightDurationMinute(currFlightSchedule.getEstimatedFlightDurationMinute());
                            nextFlightSchedule.computeAndSetArrivalDateTime();
                            List<CabinClassConfiguration> ccConfigs = flight.getAircraftConfig().getCabinClassConfigs();
            
                            for (CabinClassConfiguration cc : ccConfigs) {
                                SeatInventory seatInventory = new SeatInventory(cc.getNumSeats());
                                nextFlightSchedule.getSeatInventories().add(seatInventory);
                            }
                            
                            flightSchedules.add(nextFlightSchedule);
                            
                            currDateTime.add(DAY_OF_MONTH, nDay);
                            currFlightSchedule = nextFlightSchedule;
                            
                            //System.out.println(currDateTime);
                        }
                        
                    }
                    else if (response == 4) { // Recurrent weekly
                        flightSchedulePlan.setFlightScheduleType(FlightScheduleType.RECURRENT_WEEK);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        
                        System.out.print("Enter day of week: ");
                        Integer dayOfWeek = scanner.nextInt();
                        scanner.nextLine();
                        flightSchedulePlan.setDayOfWeek(dayOfWeek);
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        try {
                            Date date = dateFormat.parse(scanner.nextLine().trim());
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(date);
                            temp.add(DATE, 1);
                            flightSchedulePlan.setEndDate(temp.getTime());
                        }
                        catch (ParseException ex) {
                            throw new InvalidDateTimeException("Invalid end date format entered!");
                        }
                        
                        FlightSchedule currFlightSchedule = doCreateFlightSchedule(flight);
                        flightSchedules.add(currFlightSchedule);
                        
                        Calendar currDateTime = Calendar.getInstance();
                        currDateTime.setTime(currFlightSchedule.getArrivalDateTime());
                        currDateTime.add(DATE, 7);
                        
                        while (currDateTime.getTime().before(flightSchedulePlan.getEndDate())) {
                            Calendar departureDateTime = Calendar.getInstance();
                            departureDateTime.setTime(currFlightSchedule.getDepartureDateTime());
                            departureDateTime.add(DATE, 7);
                            
                            FlightSchedule nextFlightSchedule = new FlightSchedule();
                            
                            nextFlightSchedule.setFlight(flight);
                            nextFlightSchedule.setDepartureDateTime(departureDateTime.getTime());
                            nextFlightSchedule.setEstimatedFlightDurationHour(currFlightSchedule.getEstimatedFlightDurationHour());
                            nextFlightSchedule.setEstimatedFlightDurationMinute(currFlightSchedule.getEstimatedFlightDurationMinute());
                            nextFlightSchedule.computeAndSetArrivalDateTime();
                            List<CabinClassConfiguration> ccConfigs = flight.getAircraftConfig().getCabinClassConfigs();
            
                            for (CabinClassConfiguration cc : ccConfigs) {
                                SeatInventory seatInventory = new SeatInventory(cc.getNumSeats());
                                nextFlightSchedule.getSeatInventories().add(seatInventory);
                            }
                            
                            flightSchedules.add(nextFlightSchedule);
                            
                            currDateTime.add(DATE, 7);
                            currFlightSchedule = nextFlightSchedule;
                        }
                    }
                    response = 0;
                    
                    for (FlightSchedule fs : flightSchedules) {
                        //System.out.println(fs.getDepartureDateTime().toString());
                        fs.setFlightSchedulePlan(flightSchedulePlan);
                        flightSchedulePlan.getFlightSchedules().add(fs);
                    }
                    
                    List<CabinClassConfiguration> cabinClassConfigs = flight.getAircraftConfig().getCabinClassConfigs();
                    int i = 1;
                    
                    for (CabinClassConfiguration cc : cabinClassConfigs) {
                        System.out.print("Enter number of fares for Cabin Class " + i + ": ");
                        Integer numFares = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println();
                        
                        for (int j = 1; j <= numFares; j++) {
                            Fare fare = new Fare();
                            System.out.println("Fare " + j);
                            System.out.print("Input Fare basis code digits (maximum 6 digits): ");
                            String fareCode = cc.getCabinClass().toString() + scanner.next();
                            System.out.print("Input Fare amount: ");
                            BigDecimal fareAmt = scanner.nextBigDecimal();
                            
                            fare.setFareBasisCode(fareCode);
                            fare.setFareAmount(fareAmt);
                            fare.setCabinClassConfig(cc);
                            
                            fare = flightOperationSessionBeanRemote.createFare(fare);
                            
                            fare.setFlightSchedulePlan(flightSchedulePlan);
                            flightSchedulePlan.getFares().add(fare);
                        }
                        
                        System.out.println();
                        i++;
                    }
                    
                    flightSchedulePlan.setEnabled(Boolean.TRUE);
                    flightSchedulePlan.setStartDate();
                    
                    validatorSessionBeanRemote.validate(flightSchedulePlan);
                    flightSchedulePlan = flightOperationSessionBeanRemote.createFlightSchedulePlan(flightSchedulePlan);
                    
                    System.out.println("Flight Schedule Plan successfully created!\n");
                    
                    if (flight.getReturnFlight() != null) {
                        System.out.print("Return Flight exists, create return Flight Schedule Plan? (1 : Yes) :");
                        response = scanner.nextInt();
                        scanner.nextLine();
                    }
                    
                    if (response == 1) {
                        Flight returnFlight = flight.getReturnFlight();
                        
                        System.out.print("Enter layover duration: ");
                        Integer layover = scanner.nextInt();
                        scanner.nextLine();
                        
                        FlightSchedulePlan returnFlightSchedulePlan = new FlightSchedulePlan(returnFlight.getFlightNumber(), flightSchedulePlan.getFlightScheduleType());
                        
                        for (FlightSchedule fs : flightSchedulePlan.getFlightSchedules()) {
                            FlightSchedule returnFlightSchedule = new FlightSchedule();
                            
                            Calendar departure = Calendar.getInstance();
                            departure.setTime(fs.getArrivalDateTime());
                            departure.add(HOUR, layover);
                            
                            returnFlightSchedule.setFlight(returnFlight);
                            returnFlightSchedule.setDepartureDateTime(departure.getTime());
                            returnFlightSchedule.setEstimatedFlightDurationHour(fs.getEstimatedFlightDurationHour());
                            returnFlightSchedule.setEstimatedFlightDurationMinute(fs.getEstimatedFlightDurationMinute());
                            returnFlightSchedule.computeAndSetArrivalDateTime();
                            
                            List<CabinClassConfiguration> ccConfigs = returnFlight.getAircraftConfig().getCabinClassConfigs();
            
                            for (CabinClassConfiguration cc : ccConfigs) {
                                SeatInventory seatInventory = new SeatInventory(cc.getNumSeats());
                                returnFlightSchedule.getSeatInventories().add(seatInventory);
                            }
                            
                            returnFlightSchedule.setFlightSchedulePlan(returnFlightSchedulePlan);
                            returnFlightSchedulePlan.getFlightSchedules().add(returnFlightSchedule);
                        }
                        
                        for (Fare f : flightSchedulePlan.getFares()) {
                            Fare newFare = new Fare(f.getFareBasisCode(), f.getFareAmount());
                            
                            newFare.setCabinClassConfig(f.getCabinClassConfig());
                            newFare.setFlightSchedulePlan(returnFlightSchedulePlan);
                            returnFlightSchedulePlan.getFares().add(newFare);
                        }
                        
                        returnFlightSchedulePlan.setStartDate();
                        returnFlightSchedulePlan.setnDay(flightSchedulePlan.getnDay());
                        returnFlightSchedulePlan.setDayOfWeek(flightSchedulePlan.getDayOfWeek());
                        
                        if (flightSchedulePlan.getEndDate() != null) {
                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(returnFlightSchedulePlan.getFlightSchedules().get(returnFlightSchedulePlan.getFlightSchedules().size() - 1).getArrivalDateTime());
                            endDate.add(HOUR, layover);

                            returnFlightSchedulePlan.setEndDate(endDate.getTime());
                        }
                        
                        validatorSessionBeanRemote.validate(returnFlightSchedulePlan);
                        returnFlightSchedulePlan = flightOperationSessionBeanRemote.createFlightSchedulePlan(returnFlightSchedulePlan);
                        
                        flightSchedulePlan.setReturnFlightSchedulePlan(returnFlightSchedulePlan);
                        flightSchedulePlan = flightOperationSessionBeanRemote.updateFlightSchedulePlan(flightSchedulePlan);
                        System.out.println("Return Flight Schedule Plan successfully created!\n");
                    } else {
                        response = 5;
                        System.out.println("Returning to menu ...");
                        break;
                    }
                }
                catch (FlightNumberDisabledException | InvalidDateTimeException | FlightScheduleOverlapException | InvalidDurationException | FareBasisCodeExistException | ViolationException ex) {
                    System.out.println("\nFollowing error(s) occurred: " + ex.getMessage() + "\n");
                }
            }
            
            else if (response == 5) {
                System.out.println("Returning to menu ...");
                break;
            }
            else {
                System.out.println("Please enter an integer from 1 to 5\n");
            }
            
        }
        
    }
    
    private FlightSchedule doCreateFlightSchedule(Flight flight) throws InvalidDateTimeException, FlightScheduleOverlapException, InvalidDurationException {
        
        FlightSchedule flightSchedule = new FlightSchedule();
        flightSchedule.setFlight(flight);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        try {
            System.out.print("Enter date/time in format 'yyyy-MM-dd HH:mm': ");
            Date date = format.parse(scanner.nextLine().trim());
            System.out.print("Enter estimated flight duration in format 'HH:mm': ");
            String durationString = scanner.nextLine().trim();
            String[] durationElements = durationString.split(":");

            if (durationElements.length != 2) {
                throw new InvalidDurationException("Invalid duration entered!");
            }

            Integer durationHour = Integer.parseInt(durationElements[0]);
            Integer durationMinute = Integer.parseInt(durationElements[1]);
            
            flightSchedule.setDepartureDateTime(date);
            flightSchedule.setEstimatedFlightDurationHour(durationHour);
            flightSchedule.setEstimatedFlightDurationMinute(durationMinute);
            flightSchedule.computeAndSetArrivalDateTime();
            
            /*
            System.out.println(date);
            System.out.println(flightSchedule.getArrivalDateTime());
            */
            
            flightSchedule = flightOperationSessionBeanRemote.createFlightSchedule(flightSchedule); // catch FlightScheduleOverlapException
            flightSchedule.setFlight(flight);
            
            List<CabinClassConfiguration> ccConfigs = flight.getAircraftConfig().getCabinClassConfigs();
            
            for (CabinClassConfiguration cc : ccConfigs) {
                SeatInventory seatInventory = new SeatInventory(cc.getNumSeats());
                flightSchedule.getSeatInventories().add(seatInventory);
            }
            
            //flightSchedule = flightOperationSessionBeanRemote.updateFlightSchedule(flightSchedule);
            return flightSchedule;
        }
        catch (ParseException ex) {
            throw new InvalidDateTimeException("Invalid date/time format entered!");
        }
        catch (FlightScheduleOverlapException ex) {
            throw new FlightScheduleOverlapException(ex.getMessage());
        }
    }
    
    private void doViewAllFlightSchedulePlans() {
        
        System.out.println("*** View All Flight Schedule Plans ***\n");
        
        List<FlightSchedulePlan> flightSchedulePlans = flightOperationSessionBeanRemote.retrieveAllMainFlightSchedulePlans();
        
        if (flightSchedulePlans.isEmpty()) {
            System.out.println("... No Flight Schedule Plans available ...\n");
        }
        else {
            for (FlightSchedulePlan fsp : flightSchedulePlans) {
                if (fsp.getEnabled() == true) {
                    System.out.println("Flight Schedule Plan - " + fsp.getFlightNumber());
                    System.out.println("First departure date/time : " + fsp.getStartDate());
                    if (fsp.getReturnFlightSchedulePlan() != null) {
                        FlightSchedulePlan returnFsp = fsp.getReturnFlightSchedulePlan();
                        System.out.println("\tReturn Flight Schedule Plan - " + returnFsp.getFlightNumber());
                        System.out.println("\tFirst departure date/time : " + returnFsp.getStartDate());
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doViewFlightSchedulePlanDetails() {
        Integer response = 0;
        System.out.println("*** View Flight Schedule Plan Details ***\n");
        
        List<FlightSchedulePlan> flightSchedulePlans = flightOperationSessionBeanRemote.retrieveAllFlightSchedulePlans();
        
        for (FlightSchedulePlan fsp : flightSchedulePlans) {
            if (fsp.getEnabled() == true) {
                FlightRoute flightRoute = fsp.getFlightSchedules().get(0).getFlight().getFlightRoute();
                System.out.println("Flight Schedule Plan - " + fsp.getFlightNumber());
                System.out.println("Origin-Destination : " + flightRoute.getOriginAirport().getAirportCode() + "-" + flightRoute.getDestinationAirport().getAirportCode());
                
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    System.out.println(fs.getDepartureDateTime() + " --> " + fs.getArrivalDateTime());
                }
                System.out.println();
                
                for (Fare f : fsp.getFares()) {
                    System.out.println(f.getFareBasisCode() + ": " + f.getFareAmount());
                }
                
                System.out.print("(0 : skip, 1 : select, other integer : exit) :");
                
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                
                if (response == 0) {
                    // skip
                }
                if (response == 1) {
                    response = 0;
                    System.out.println("1: Update Flight Schedule Plan");
                    System.out.println("2: Delete Flight Schedule Plan");
                    System.out.println("Other integer: Return");
                    
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (response == 1) {
                        doUpdateFlightSchedulePlan(fsp);
                    }
                    else if (response == 2) {
                        doDeleteFlightSchedulePlan(fsp);
                    }
                    else {
                        break;
                    }
                }
                else {
                    System.out.println();
                    break;
                }
            }
        }
        
        System.out.println("... Press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doUpdateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        System.out.println("*** Update Flight Schedule Plan ***\n");
        
        //List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
        List<Fare> fares = flightSchedulePlan.getFares();
        
        /*
        for (FlightSchedule fs : flightSchedules) {
            
        }
        */
        
        for (Fare f : fares) {
            System.out.println("Fare " + f.getFareBasisCode());
            System.out.print("Enter new fare amount: ");
            BigDecimal newAmt = scanner.nextBigDecimal();
            scanner.nextLine();
            
            if (newAmt.compareTo(new BigDecimal(0)) < 0) { // newAmt negative
                System.out.println("\nFare Amount must be positive!\n");
                break;
            }
            else {
                f.setFareAmount(newAmt);
                flightOperationSessionBeanRemote.updateFare(f);

                System.out.println("Fare updated!\n");
            }
        }
        
        System.out.println("... Press enter to return ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doDeleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        
        System.out.println("*** Delete Flight Schedule Plan ***\n");
        
        List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
        
        for (FlightSchedule fs : flightSchedules) {
            if (!fs.getFlightReservations().isEmpty()) {
                flightSchedulePlan.setEnabled(Boolean.FALSE);
                System.out.println("Flight Schedule Plan disabled.\n");
                return;
            }
        }
        
        System.out.println("Confirm deletion? (Y : delete)");
        String response = scanner.nextLine().trim();
        System.out.println();
        
        if (response.equalsIgnoreCase("Y")) {
            flightOperationSessionBeanRemote.deleteFlightSchedulePlan(flightSchedulePlan);
            System.out.println("Flight Schedule Plan successfully deleted!\n");
        }
        else {
            System.out.println("Deletion cancelled, returning to menu ...\n");
        }
    }
    
    /*
    Sales Manager methods
    */
    
    private void doViewSeatsInventory() {
        Integer response = 0;
        System.out.println("*** View Seats Inventory ***\n");
        
        System.out.println("Enter Flight Number: ");
        String flightNumber = scanner.nextLine().trim();
        
        try {
            Flight flight = flightOperationSessionBeanRemote.retrieveFlightByNumber(flightNumber);
            
            if (flight.getFlightSchedules().isEmpty()) {
                System.out.println("... No Flight Schedules available ...\n");
            }
            else {
                System.out.println("Choose Flight Schedule (0 : skip, 1 : select, other integer : exit)");

                for (int i = 0; i < flight.getFlightSchedules().size(); i++) {
                    FlightSchedule fs = flight.getFlightSchedules().get(i);

                    System.out.print("Flight Schedule " + (i+1) + " - " + fs.getDepartureDateTime() + " : ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();

                    if (response == 0) {
                        //skip
                    }
                    else if (response == 1) {

                        for (int j = 0; j < fs.getSeatInventories().size(); j++) {
                            CabinClassConfiguration cc = flight.getAircraftConfig().getCabinClassConfigs().get(j);
                            SeatInventory si = fs.getSeatInventories().get(j);

                            System.out.println("Cabin Class - " + cc.getCabinClass());
                            System.out.println("Available Seats : " + si.getAvailable());
                            System.out.println("Reserved Seats : " + si.getReserved());
                            System.out.println("Balance Seats : " + si.getBalance());

                            System.out.println();
                        }

                    }
                    else {
                        break;
                    }
                }
            }
        }
        catch (FlightNumberDisabledException ex) {
            System.out.println("Following error occurred: " + ex.getMessage());
        }
        
        System.out.println();
        System.out.println("... Press enter to return ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doViewFlightReservations() {
        Integer response = 0;
        System.out.println("*** View Flight Reservations ***\n");
        
        System.out.println("Enter Flight Number: ");
        String flightNumber = scanner.nextLine().trim();
        System.out.println();
        
        try {
            Flight flight = flightOperationSessionBeanRemote.retrieveFlightByNumber(flightNumber);
            
            if (flight.getFlightSchedules().isEmpty()) {
                System.out.println("... No Flight Schedules available ...\n");
            }
            else {
                System.out.println("Choose Flight Schedule (0 : skip, 1 : select, other integer : exit)");

                for (int i = 0; i < flight.getFlightSchedules().size(); i++) {
                    FlightSchedule fs = flight.getFlightSchedules().get(i);

                    System.out.print("Flight Schedule " + (i+1) + " - " + fs.getDepartureDateTime() + " : ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println();

                    if (response == 0) {
                        //skip
                    }
                    else if (response == 1) {

                        if (fs.getFlightReservations().isEmpty()) {
                            System.out.println("... No Existing Flight Reservations ...\n");
                            break;
                        }

                        for (int j = 0; j < flight.getAircraftConfig().getCabinClassConfigs().size(); j++) {
                            CabinClassConfiguration cc = flight.getAircraftConfig().getCabinClassConfigs().get(j);

                            List<FlightReservation> flightReservations = flightOperationSessionBeanRemote.retrieveFlightReservationsByCabinClass(cc);

                            if (!flightReservations.isEmpty()) {
                                System.out.println("Cabin Class - " + cc.getCabinClass());

                                for (FlightReservation fr : flightReservations) {
                                    System.out.println("Seat Number: " + fr.getSeatNumber());
                                    System.out.println("Passenger name: " + fr.getCustomer().getFirstName() + " " + fr.getCustomer().getLastName());
                                    System.out.println("Flight Basis Code: " + fs.getFlightSchedulePlan().getFares().get(j));
                                }

                            }
                            System.out.println();
                        }

                    }
                    else {
                        break;
                    }
                }
            }
        }
        catch (FlightNumberDisabledException ex) {
            System.out.println("Following error occurred: " + ex.getMessage());
        }
        
        System.out.println();
        System.out.println("... Press enter to return ...");
        scanner.nextLine();
        System.out.println();
    }
}
