/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.DataInputException;
import util.exception.InvalidCredentialsException;
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
    
    public void runApp() throws UsernameAlreadyTakenException, InvalidCredentialsException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Merlion Airlines ***\n");
            System.out.println("1: Register as Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Flights");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    registerAsCustomer();
                } else if(response == 2) {
                    doCustomerLogin();
                } else if(response == 3) {
                    // searchFlight();
                    System.out.println("searchFlight not done yet rip");
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
    }
    
    public void registerAsCustomer() throws UsernameAlreadyTakenException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Register as Customer ***\n");
        Customer newCustomer = new Customer();
        System.out.println("Enter First Name: ");
        newCustomer.setFirstName(sc.nextLine().trim());
        System.out.println("Enter Last Name: ");
        newCustomer.setLastName(sc.nextLine().trim());
        System.out.println("Enter Email: ");
        newCustomer.setEmail(sc.nextLine().trim());
        System.out.println("Enter Mobile Phone Number: ");
        newCustomer.setMobileNumber(sc.nextLine().trim());
        System.out.println("Enter Address: ");
        newCustomer.setAddress(sc.nextLine().trim());
        System.out.println("Enter Username: ");
        newCustomer.setUsername(sc.nextLine().trim());
        System.out.println("Enter Password: ");
        newCustomer.setPassword(sc.nextLine().trim());
        
        try {
            Long newCustomerId = customerSessionBeanRemote.createCustomer(newCustomer);
        } catch (UsernameAlreadyTakenException ex) {
            System.out.println("This Username is already taken, please try another!");
        }
        
        System.out.println("New customer " + newCustomer.getUsername() + " created! Customer ID number: " + newCustomer.getCustomerId());
        System.out.println();
    }
    
    public void doCustomerLogin() throws InvalidCredentialsException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Login as Customer ***\n");
        System.out.println("Username: ");
        String username = sc.nextLine().trim();
        System.out.println("Password: ");
        String password = sc.nextLine().trim();
        System.out.println();
        
        try {
            loggedInCustomer = customerSessionBeanRemote.login(username, password);
        } catch (InvalidCredentialsException ex) {
            System.out.println("The username or password is incorrect, please try again!");
        }
        
        postLogin();
    }
    
//    public void searchFlight() throws DataInputException {
//        
//        Scanner sc = new Scanner(System.in);
//        System.out.println("*** Search for Flight ***\n");
//        
//        System.out.println("Select Trip Type (1 for one-way, 2 for return): ");
//        int tripType = sc.nextInt();
//        
//        System.out.println("Departure Airport: ");
//        String departureAirport = sc.nextLine().trim();
//        
//        System.out.println("Destination Airport: ");
//        String destinationAirport = sc.nextLine().trim();
//        
//        System.out.println("Departure Date (DDMMYYYY): ");
//        String departureDate = sc.nextLine().trim();
//        
//        if (tripType == 2) {
//            System.out.println("Return Date (DDMMYYYY): ");
//            String returnDate = sc.nextLine().trim();
//        }
//        
//        System.out.println("Number of Passengers: ");
//        int numOfPassengers = sc.nextInt();
//        
////        System.out.println("Would you prefer a direct flight or connecing flight?");
////        System.out.println("Enter '1' for direct, '2' for connecting: ");
////        int directOrConnecting = sc.nextInt();
//
////        preference for cabin class
//        
//        
//        
//        List<FlightSchedule> listOfFlights = customerSessionBeanRemote.makeSearch(departureAirport, destinationAirport, destinationAirport, numOfPassengers);
//        System.out.println("*** " + departureAirport + " to " + destinationAirport + " ***\n");
//        for (FlightSchedule f : listOfFlights) {
//            System.out.println();
//        }
//        
//        if (tripType == 2) {
//            List<FlightSchedule> listOfReturnFlights = customerSessionBeanRemote.makeSearch();
//        }
//        
//    }
    
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
    
    public void viewFlightReservations() {
        
        List<FlightReservation> listOfFlightReservations = customerSessionBeanRemote
                .retrieveFlightReservations(loggedInCustomer.getCustomerId());
        System.out.println("Departure Date  /   Departure Time  /   Departure Airport   /   ");
        for (FlightReservation fr : listOfFlightReservations) {
            
        }
    }
    
    public void viewFlightReservationDetail() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Which reservation would you like to view? (0 to go to next reservation, 1 to view)");
        Integer response = 2;
        FlightReservation flightReservation = new FlightReservation();
        String departureDate = "09112020"; // stub
        String departureTime = "2300"; // stub
        String departureAirport = "SGP"; // stub
        String destinationAirport = "TKY"; // stub
        int numOfPassengers = 0; // stub
        List<FlightReservation> listOfFlightReservations = customerSessionBeanRemote
                .retrieveFlightReservations(loggedInCustomer.getCustomerId());
        int numOfReservations = listOfFlightReservations.size();
        for (int i = 0; i < numOfReservations; i++) {
            flightReservation = listOfFlightReservations.get(i);
            departureDate = "09112020"; // stub
            departureTime = "2300"; // stub
            departureAirport = "SGP"; // stub
            destinationAirport = "TKY"; // stub
            numOfPassengers = 0; // stub
            System.out.print("" + departureDate + "   " + departureTime + "   " + departureAirport + " - " + destinationAirport 
                    + ", " + numOfPassengers + " Passenger(s): ");
            response = sc.nextInt();
            if (response == 1) {
                break;
            }
        }
        System.out.println();
        
        FlightSchedule flightSchedule = flightReservation.getFlightSchedule();
        Flight flight = flightSchedule.getFlight();
        FlightRoute flightRoute = flight.getFlightRoute();
        
        
        
        System.out.println("*** Flight Reservation Details *** \n");
        // for every flight
        System.out.println("Departure: " + departureDate + "   " + departureDate + "   " + departureTime + " timezone");
        System.out.println("  Arrival: " + departureDate + "   " + departureDate + "   " + departureTime + " timezone\n");
        
        // for every passenger
        System.out.println("Passengers:");
        System.out.println("First Name " + "Last Name" + "   " + "Passport Number");
            // for every flight
            System.out.println("departureAirport - arrivalAirport : Cabin class, seatnumber");
        
        
        System.out.println("totalAmountPaid"); // input money
    }
    
    public void doLogout() {
        loggedInCustomer = null;
        System.out.println("You have logged out successfully.\n");
    }
}
