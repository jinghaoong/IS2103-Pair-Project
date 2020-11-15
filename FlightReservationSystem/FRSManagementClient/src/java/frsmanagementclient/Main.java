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
import javax.ejb.EJB;

/**
 *
 * @author jinghao
 */
public class Main {
    
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static FlightPlanningSessionBeanRemote flightPlanningSessionBeanRemote;
    @EJB
    private static FlightOperationSessionBeanRemote flightOperationSessionBeanRemote;
    @EJB
    private static ValidatorSessionBeanRemote validatorSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        if(employeeSessionBeanRemote == null)
            System.out.println("********** IS NULL");
        else
            System.out.println("********** NOT null");
        */
        
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, flightPlanningSessionBeanRemote, flightOperationSessionBeanRemote, validatorSessionBeanRemote);
        mainApp.runApp();
    }
    
}
