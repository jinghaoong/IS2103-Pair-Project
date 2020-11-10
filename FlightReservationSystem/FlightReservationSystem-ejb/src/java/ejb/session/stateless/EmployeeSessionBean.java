/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author jinghao
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Employee employeeLogin(String username, String password) throws EmployeeNotFoundException {
        
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        
        try {
            return (Employee)query.getSingleResult();
        }
        catch (NoResultException ex)
        {
            throw new EmployeeNotFoundException("Employee not found with given username and password");
        }
        
    }
    
}
