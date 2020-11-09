/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightPlanningSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CabinClass;
import util.enumeration.EmployeeRole;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.FlightRouteAlreadyExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.MaximumSeatCapacityExceededException;
import util.exception.NumberOfSeatsPerRowMismatchException;

/**
 *
 * @author jinghao
 */
public class MainApp {
    
    private Scanner scanner;
    private Employee employee;
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote;
    
    
    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.flightPlanningSessionBeanRemote = flightPlanningSessionBeanRemote;
    }
    
    public void runApp() {
        
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
                        employee = doLogin();
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
                            
                            System.out.println("***** FRS Management - Flight Operation *****\n");
                            System.out.println("Welcome, " + employee.getName() + "!\n");
                        }
                        else { // employeeRole == EmployeeRole.SALES_MANAGER
                            
                            System.out.println("***** FRS Management - Sales Management *****\n");
                            System.out.println("Welcome, " + employee.getName() + "!\n");
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
    
    private Employee doLogin() throws EmployeeNotFoundException {
        
        System.out.print("Enter Employee username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Employee password: ");
        String password = scanner.nextLine().trim();
        System.out.println();
        
        try {
            Employee employee = employeeSessionBeanRemote.employeeLogin(username, password);
            System.out.println("Employee login successful!\n");
            return employee;
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
                        System.out.println("Following error occurred: " + ex.getMessage());
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
                flightPlanningSessionBeanRemote.setReturnFlightRoute(flightRoute.getFlightRouteId(), returnFlightRoute.getFlightRouteId());
                
                System.out.println("Return Flight Route " + returnOriginAirport.getAirportCode() + "-" + returnDestinationAirport.getAirportCode() + " successfully created!\n");
            }
            
            System.out.println("... Press enter to continue ...");
            scanner.nextLine();
        }
        catch (AirportNotFoundException | FlightRouteAlreadyExistException ex) {
            System.out.println("Following error occurred: " + ex.getMessage());
        }
    }
    
    private void doViewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = flightPlanningSessionBeanRemote.retrieveAllFlightRoutes();
        
        System.out.println("*** List of Flight Routes ***\n");
        
        for (FlightRoute fr : flightRoutes) {
            System.out.println("Flight Route: " + fr.getOriginAirport().getAirportCode() + "-" + fr.getDestinationAirport().getAirportCode());
            if (fr.getReturnFlightRoute() != null) {
                System.out.println("\tReturn: " + fr.getDestinationAirport().getAirportCode() + "-" + fr.getOriginAirport().getAirportCode());
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
    
    /*
    Sales Manager methods
    */
    
}
