/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightPlanningSessionBeanRemote;
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, flightPlanningSessionBeanRemote);
        mainApp.runApp();
    }
    
}
