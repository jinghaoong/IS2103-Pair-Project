/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author yylow
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public AircraftConfiguration createAircraftConfiguration(AircraftConfiguration newAircraftConfig) {
        
        em.persist(newAircraftConfig);
        em.flush();
        
        return newAircraftConfig;
    }
    
    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigs() {
        
        TypedQuery<AircraftConfiguration> query = em.createQuery("SELECT a FROM AircraftConfiguration a", AircraftConfiguration.class);
        List<AircraftConfiguration> aircraftConfigs = query.getResultList();
        // sort by aircraft type
        aircraftConfigs.sort((AircraftConfiguration ac1, AircraftConfiguration ac2) -> 
                    ac1.getAircraftConfigName().compareTo(ac2.getAircraftConfigName()));
        aircraftConfigs.sort((AircraftConfiguration ac1, AircraftConfiguration ac2) -> 
                    ac1.getAircraftType().getAircraftTypeName().compareTo(ac2.getAircraftType().getAircraftTypeName()));
        
        return aircraftConfigs;
    }

    @Override
    public AircraftConfiguration viewAircraftConfig(String aircraftConfigName) {
        
        Query query = em.createQuery("SELECT a FROM AircraftConfiguration a WHERE a.aircraftConfigName = :aircraftConfigName")
                .setParameter("aircraftConfigName", aircraftConfigName);
        // try catch
        AircraftConfiguration aircraftConfig = (AircraftConfiguration) query.getSingleResult();
        
        return aircraftConfig;
    }
    
}
