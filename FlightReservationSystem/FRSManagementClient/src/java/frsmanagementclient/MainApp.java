/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightOperationSessionBeanRemote;
import ejb.session.stateless.FlightPlanningSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClass;
import util.enumeration.EmployeeRole;
import util.enumeration.FlightScheduleType;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.FlightNumberDoesNotExistException;
import util.exception.FlightRouteAlreadyExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightScheduleOverlapException;
import util.exception.InvalidDateTimeException;
import util.exception.InvalidDurationException;
import util.exception.MaximumSeatCapacityExceededException;
import util.exception.NumberOfSeatsPerRowMismatchException;

/**
 *
 * @author jinghao
 */
public class MainApp {
    
    private Scanner scanner;
    private Employee employee;
    private Integer flightNumberIncrement = 5;
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote;
    private FlightOperationSessionBeanRemote flightOperationSessionBeanRemote;
    
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote, FlightOperationSessionBeanRemote flightOperationSessionBeanRemote) {
        
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightPlanningSessionBeanRemote = flightPlanningSessionBeanRemote;
        this.flightOperationSessionBeanRemote = flightOperationSessionBeanRemote;
        
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
                response = scanner.nextInt();
                scanner.nextLine();
                
                System.out.println();
                
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
                response = scanner.nextInt();
                scanner.nextLine();
                
                System.out.println();
                
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
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                
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
                    doViewFlightScheduleDetails(); // includes Update, Delete
                }
                else if (response == 7) {
                    doLogout();
                    break;
                }
                else {
                    System.out.println("Please input an integer from 1 to 7\n");
                }
            }
            
            if (response == 7) {
                break;
            }
        }
    }
    
    private void salesManagerMenu() { 
        
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
            System.out.println("Select Aircraft Type (0 : skip , 1 : select)");
            response = 0;
            for (Integer i = 0; i < aircraftTypes.size(); i++) {
                System.out.print("Aircraft Type - " + aircraftTypes.get(i).getAircraftTypeName() + ": ");
                response = scanner.nextInt();
                
                if (response == 1) {
                    aircraftType = aircraftTypes.get(i);
                    scanner.nextLine();
                    break;
                }
            }
            
            if (response == 0) {
                System.out.println("No aircraft type selected!");
                
                System.out.println("... Press enter to return ...");
                scanner.nextLine();
                System.out.println();
                return;
            }
            if (response == 1) {
                break;
            }
        }
        
        System.out.print("Enter Aircraft Configuration Name: "); // unique
        String name = scanner.nextLine().trim();
        System.out.print("Enter number of Cabin Classes: "); // 1 - 4
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
        flightPlanningSessionBeanRemote.createAircraftConfig(aircraftConfig);
        System.out.println("Aircraft config '" + aircraftConfig.getAircraftConfigName() + "' successfully created!\n");
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
        
            System.out.println("Select Aircraft Configuration to view (0 : skip, 1 : select)");
            
            for (AircraftConfiguration ac : aircraftConfigs) {
                System.out.print(ac.getAircraftConfigName() + ": ");
                response = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                
                if (response == 1) {
                    System.out.println("Aircraft Configuration Name : " + ac.getAircraftConfigName());
                    System.out.println("Number of Cabin Classes : " + ac.getNumberOfCabinClass());
                    System.out.println();
                    
                    for (Integer i = 1; i <= ac.getNumberOfCabinClass(); i++) {
                        System.out.println("Cabin Class " + i + " Details");
                        System.out.println("Cabin Class Type : " + ac.getCabinClassConfigs().get(i-1).getCabinClass());
                        System.out.println("Number of Rows : " + ac.getCabinClassConfigs().get(i-1).getNumberOfRows());
                        
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
        catch (AirportNotFoundException | FlightRouteAlreadyExistException ex) {
            System.out.println("Following error occurred: " + ex.getMessage() + "\n");
        }
    }
    
    private void doViewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = flightPlanningSessionBeanRemote.retrieveAllFlightRoutes();
        
        System.out.println("*** List of Flight Routes ***\n");
        
        for (FlightRoute fr : flightRoutes) {
            System.out.println("Flight Route: " + fr.getOriginAirport().getAirportName() + " -> " + fr.getDestinationAirport().getAirportName());
            if (fr.getReturnFlightRoute() != null) {
                System.out.println("\tReturn: " + fr.getDestinationAirport().getAirportName() + " -> " + fr.getOriginAirport().getAirportName());
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
        String largestFlightNumber = flightOperationSessionBeanRemote.retrieveLargestFlightNumber();
        Integer largestFlightDigits = Integer.parseInt(largestFlightNumber.split("ML")[1]);
        Integer number = largestFlightDigits + flightNumberIncrement;
        
        System.out.println("*** Create Flight ***\n");
        String flightNumber = "ML" + number;
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
        
        Flight newFlight = new Flight(flightNumber);
        newFlight.setFlightRoute(flightRoute);
        newFlight.setAircraftConfig(aircraftConfig);
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
                String returnFlightNumber = "ML" + (number+1);
                
                Flight newReturnFlight = new Flight(returnFlightNumber);
                
                newReturnFlight.setFlightRoute(returnFlightRoute);
                newReturnFlight.setAircraftConfig(aircraftConfig);
                newReturnFlight = flightOperationSessionBeanRemote.createFlight(newReturnFlight);
                
                newFlight.setReturnFlight(newReturnFlight);
                flightOperationSessionBeanRemote.updateFlight(newFlight);
                
                System.out.println("Return Flight " + newReturnFlight.getFlightNumber()+ " successfully created!");
            }
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
            System.out.println("Flight: " + f.getFlightNumber() + " " + f.getFlightRoute().getOriginAirport().getAirportCode() + "-" + f.getFlightRoute().getDestinationAirport().getAirportCode());
            if (f.getReturnFlight() != null) {
                Flight rf = f.getReturnFlight();
                System.out.println("\tReturn: " + rf.getFlightNumber() + " " + rf.getFlightRoute().getOriginAirport().getAirportCode() + "-" + rf.getFlightRoute().getDestinationAirport().getAirportCode());
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
        
        System.out.println("Select Flight to view (0 : skip, 1 : select)");
        for (Flight f : flights) {
            System.out.print("Flight " + f.getFlightNumber() + ": ");
            response = scanner.nextInt();
            scanner.nextLine();
            
            if (response == 1) {
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
                
                response = 0;
                
                while (response < 1 || response > 3) {
                    System.out.println("1: Update Flight");
                    System.out.println("2: Delete Flight");
                    System.out.println("3: Return to next Flight\n");
                    
                    System.out.print("> ");
                    response = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (response == 1) {
                        doUpdateFlight(f);
                    }
                    else if (response == 2) {
                        doDeleteFlight(f);
                    }
                    else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Please select an integer from 1 to 3.\n");
                    }
                }
            }
        }
        
        System.out.println();
        System.out.print("... No more Flights, press enter to continue ...");
        scanner.nextLine();
        System.out.println();
    }
    
    private void doUpdateFlight(Flight flight) {
        Integer response = 0;
        System.out.println("*** Update Flight Details ***\n");
        
        // tbd
        
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
                        flightSchedules.add(doCreateFlightSchedule(flight));
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
                            flightSchedulePlan.setEndDate(dateFormat.parse(scanner.nextLine().trim()));
                        }
                        catch (ParseException ex) {
                            throw new InvalidDateTimeException("Invalid end date format entered!");
                        }
                        
                        FlightSchedule firstFlightSchedule = doCreateFlightSchedule(flight);
                        
                        Calendar currDateTime = Calendar.getInstance();
                        currDateTime.setTime(firstFlightSchedule.getDepartureDateTime());
                        
                        
                        
                        
                    }
                    else if (response == 4) { // Recurrent weekly
                        flightSchedulePlan.setFlightScheduleType(FlightScheduleType.RECURRENT_WEEK);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        
                        System.out.print("Enter day of week: ");
                        flightSchedulePlan.setDayOfWeek(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        try {
                            flightSchedulePlan.setEndDate(dateFormat.parse(scanner.nextLine().trim()));
                        }
                        catch (ParseException ex) {
                            throw new InvalidDateTimeException("Invalid end date format entered!");
                        }
                        
                        FlightSchedule firstFlightSchedule = doCreateFlightSchedule(flight);
                    }
                    
                    
                    for (FlightSchedule fs : flightSchedules) {
                        flightSchedulePlan.getFlightSchedules().add(fs);
                    }
                    
                    /*
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
                            String fareCode = cc.getCabinClass().toString() + scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Input Fare amount: ");
                            BigDecimal fareAmt = scanner.nextBigDecimal();
                            
                            fare.setFareBasisCode(fareCode);
                            fare.setFareAmount(fareAmt);
                            fare.setCabinClassConfig(cc);
                            
                            //fare = flightOperationSessionBeanRemote.createFare(fare);
                            
                            cc.getFares().add(fare);
                            flightOperationSessionBeanRemote.updateCabinClassConfiguration(cc);
                        }
                        
                        System.out.println();
                        i++;
                    }
                    */
                    flightSchedulePlan = flightOperationSessionBeanRemote.updateFlightSchedulePlan(flightSchedulePlan);
                }
                catch (FlightNumberDoesNotExistException | InvalidDateTimeException | FlightScheduleOverlapException | InvalidDurationException ex) {
                    System.out.println("Following error occurred: " + ex.getMessage());
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
            
            flightSchedule = flightOperationSessionBeanRemote.createFlightSchedule(flightSchedule); // catch FlightScheduleOverlapException
            
            List<CabinClassConfiguration> ccConfigs = flight.getAircraftConfig().getCabinClassConfigs();
            
            for (CabinClassConfiguration cc : ccConfigs) {
                SeatInventory seatInventory = new SeatInventory(cc.getNumSeats());
                flightSchedule.getSeatInventories().add(seatInventory);
            }
            
            flightSchedule = flightOperationSessionBeanRemote.updateFlightSchedule(flightSchedule);
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
        
    }
    
    private void doViewFlightScheduleDetails() {
        
    }
    
    /*
    Sales Manager methods
    */
    
}
