/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Airport;
import entity.Customer;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.EmailAlreadyInUseException;
import util.exception.InvalidCredentialsException;
import util.exception.MobileNumberAlreadyInUseException;
import util.exception.UsernameAlreadyTakenException;

/**
 *
 * @author yylow
 */
public class MainApp {
    
    private Customer loggedInCustomer;
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }
    
    public void runApp() throws UsernameAlreadyTakenException, InvalidCredentialsException, EmailAlreadyInUseException, MobileNumberAlreadyInUseException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Merlion Airlines ***\n");
            System.out.println("1: Register as Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Flights");
            System.out.println("Enter any other integer to exit.\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    registerAsCustomer();
                } else if(response == 2) {
                    doCustomerLogin();
                } else if(response == 3) {
                    searchFlight();
                    //System.out.println("searchFlight to be completed by Sunday\n");
                } else {
                    break;
                }
            }
            if(response > 3) {
                break;
            }
        }
    }
    
    public void registerAsCustomer() throws UsernameAlreadyTakenException, EmailAlreadyInUseException, MobileNumberAlreadyInUseException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Register as Customer ***\n");
        Customer newCustomer = new Customer();
        System.out.print("Enter First Name: ");
        newCustomer.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name: ");
        newCustomer.setLastName(sc.nextLine().trim());
        System.out.print("Enter Email: ");
        newCustomer.setEmail(sc.nextLine().trim());
        System.out.print("Enter Mobile Phone Number: ");
        newCustomer.setMobileNumber(sc.nextLine().trim());
        System.out.print("Enter Address: ");
        newCustomer.setAddress(sc.nextLine().trim());
        System.out.print("Enter Username: ");
        newCustomer.setUsername(sc.nextLine().trim());
        System.out.print("Enter Password: ");
        newCustomer.setPassword(sc.nextLine().trim());
        
        Long newCustomerId = 0l;
        try {
            newCustomerId = customerSessionBeanRemote.createCustomer(newCustomer);
            System.out.println("New customer " + newCustomer.getUsername() + " created! Customer ID number: " + newCustomerId);
            System.out.println();
        } catch (UsernameAlreadyTakenException | EmailAlreadyInUseException | MobileNumberAlreadyInUseException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        }
    }
    
    public void doCustomerLogin() throws InvalidCredentialsException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Login as Customer ***\n");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        System.out.println();
        
        try {
            loggedInCustomer = customerSessionBeanRemote.login(username, password);
            postLogin();
        } catch (InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        }
    }
    
    public Flight searchFlight() {
        
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("*** Search for Flight ***\n");
        System.out.print("Select Trip Type (1 for one-way, 2 for return): ");
        int tripType = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Departure Airport: ");
        String departureAirport = sc.nextLine().trim();
        System.out.print("Destination Airport: ");
        String destinationAirport = sc.nextLine().trim();
        System.out.print("Number of Passengers: ");
        Integer numOfPassengers = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Departure Date (YYYY-MM-DD): ");
        String departure = sc.nextLine().trim();
        Date departureDate = new Date();
        try {
            departureDate = dateFormat.parse(departure);
        } catch (ParseException ex){
            System.out.println("Please follow the given format \"YYYY-MM-DD\"");
        }
        
        String returnDeparture = "";
        Date returnDepartureDate = new Date();
        if (tripType == 2) {
            System.out.println("Return Departure Date (YYYY-MM-DD): ");
            returnDeparture = sc.nextLine().trim();
            try {
                returnDepartureDate = dateFormat.parse(departure);
            } catch (ParseException ex){
                System.out.println("Please follow the given format \"YYYY-MM-DD\"");
            }
        }
        
        System.out.print("Do you prefer a direct flight (1) or connecting flight (2)?: ");
        int directOrConnecting = sc.nextInt();
        sc.nextLine();
        System.out.println("Please select a cabin class.");
        System.out.print("First Class (F), Business (J), Premium Economy (W), Economy (Y), No Preference (N): ");
        String cabinClass = sc.nextLine().trim();
        
        List<Flight> directFlights = new ArrayList<>();
        List<List<Flight>> connectingFlights = new ArrayList<>();

        if (directOrConnecting == 1) {
            directFlights = customerSessionBeanRemote.makeDirectSearch(departureAirport, destinationAirport, 
                departureDate, numOfPassengers, cabinClass);
            
        }
        
        if (directOrConnecting == 2) {
            connectingFlights = customerSessionBeanRemote.makeConnectingSearch(departureAirport, destinationAirport, 
                departureDate, numOfPassengers, cabinClass);
        }
        
        if (tripType == 2) { // return flight
            if (directOrConnecting == 1) {
                directFlights = customerSessionBeanRemote.makeDirectSearch(destinationAirport, departureAirport, 
                    returnDepartureDate, numOfPassengers, cabinClass);
            
            }
            
            if (directOrConnecting == 2) {
            connectingFlights = customerSessionBeanRemote.makeConnectingSearch(destinationAirport, departureAirport, 
                returnDepartureDate, numOfPassengers, cabinClass);
            
            }
        }
        
        
        
        if (loggedInCustomer == null) {
            return new Flight(); //
        } else {
            System.out.print("Select a Flight Number");
            return new Flight();
        }
    }

    
    public void postLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** You have successfully logged in! ***\n");
        Integer response = 0;
        
        while(true) {
            System.out.println("You are now logged in, " + loggedInCustomer.getFirstName() + ". What would you like to do today?");
            System.out.println("1: Reserve Flight");
            System.out.println("2: View My Flight Reservations");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if(response == 1) {
                    reserveFlight();
                } else if(response == 2) {
                    viewFlightReservations();
                } else if(response == 3) {
                    viewFlightReservationDetail();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            if(response == 4) {
                break;
            }
        }
        if (response == 4) {
            doLogout();
        }
    }
    
    public void reserveFlight() {
        
    }
    
    public void viewFlightReservations(){
        System.out.println("*** View Flight Reservations ***\n");
        
        Long customerId;
        if (loggedInCustomer != null) {
            customerId = loggedInCustomer.getCustomerId();
        } else {
            customerId = 0l;
        }
        List<FlightReservation> listOfFlightReservations = customerSessionBeanRemote.retrieveFlightReservations(customerId);

        if (listOfFlightReservations.isEmpty()) {
            System.out.println("No Flight Reservations have been made. Make a reservation today!\n");
            //System.out.println("Date\t\tTime\tFlight Number\tOrigin\tDestination\n");
            //System.out.println("1234-56-78\t69:69\tMH370\t\tMAL\tNIL\n");
        } else {
            System.out.println("Date\t\tTime\tFlight Number\tOrigin\tDestination\n");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\thh:mm");
            FlightSchedule flightSchedule;
            Date departure;
            String departureFormat;
            Flight flight;
            String flightNum;
            FlightRoute flightRoute;
            Airport airport;
            String origin;
            String destination;
            for (FlightReservation fr : listOfFlightReservations) {
                flightSchedule = fr.getFlightSchedule();
                departure = flightSchedule.getDepartureDateTime();
                departureFormat = format.format(departure);
                flight = flightSchedule.getFlight();
                flightNum = flight.getFlightNumber();
                flightRoute = flight.getFlightRoute();
                airport = flightRoute.getOriginAirport();
                origin = airport.getAirportCode();
                airport = flightRoute.getDestinationAirport();
                destination = airport.getAirportCode();
                
                System.out.println(departureFormat + "\t" + flightNum + "\t\t" + origin + "\t" + destination);
            }
            System.out.println();
        }
    }
    
    public void viewFlightReservationDetail() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Flight Reservation Details ***\n");
        
        Long customerId;
        if (loggedInCustomer != null) {
            customerId = loggedInCustomer.getCustomerId();
        } else {
            customerId = 0l;
        }
        List<FlightReservation> listOfFlightReservations = customerSessionBeanRemote.retrieveFlightReservations(customerId);

        if (listOfFlightReservations.isEmpty()) {
            System.out.println("No Flight Reservations have been made. Make a reservation today!\n");
            //System.out.println("Date\t\tTime\tFlight Number\tOrigin\tDestination\n");
            //System.out.println("1234-56-78\t69:69\tMH370\t\tMAL\tNIL\n");
        } else {
            System.out.println("Which reservation would you like to view? (enter 0 to go to next, 1 to view)");
            int response = 2;
            //System.out.println("Date\t\tTime\tFlight Number\tOrigin\tDestination\n");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\thh:mm");
            FlightSchedule flightSchedule;
            Date departure;
            String departureFormat;
            Flight flight;
            String flightNum;
            FlightRoute flightRoute;
            Airport airport;
            String origin;
            String destination;
            int numOfReservations = listOfFlightReservations.size();
            for (int i = 0; i < numOfReservations; i++) {
                FlightReservation flightReservation = listOfFlightReservations.get(i);
                flightSchedule = flightReservation.getFlightSchedule();
                departure = flightSchedule.getDepartureDateTime();
                departureFormat = format.format(departure);
                flight = flightSchedule.getFlight();
                flightNum = flight.getFlightNumber();
                flightRoute = flight.getFlightRoute();
                airport = flightRoute.getOriginAirport();
                origin = airport.getAirportCode();
                airport = flightRoute.getDestinationAirport();
                destination = airport.getAirportCode();
                
                System.out.print(departureFormat + "\t" + flightNum + "\t" + origin + "-" + destination + ": ");
                response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    System.out.println("Here are the reservation details.\n");
                    // get flights! (bc got connecting)                    
                    // get number of passengers
                    // for each flight
                    System.out.println("-------------Flight Details-------------");
                    System.out.println("Flight Number: ");
                    System.out.println("Departure Date and Time: ");
                    System.out.println("Origin Airport: ");
                    System.out.println("Destination Airport: ");
                    System.out.println("Arrival Date and Time: ");
                    System.out.println("Estimated Flight Duration: \n");
                    System.out.println("Number of passengers: ");
                    System.out.println("-------------Passenger Details-------------");
                    // for each passenger
                        System.out.println("Name of passenger: ");
                        System.out.println("Cabin class: ");
                        System.out.println("Seat number: ");
                        // System.out.println("Departure date: "); template
                    System.out.println();
                    System.out.println("Total Amount Paid: $");
                    break;
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    public void doLogout() {
        loggedInCustomer = null;
        System.out.println("You have logged out successfully.\n");
    }
}
