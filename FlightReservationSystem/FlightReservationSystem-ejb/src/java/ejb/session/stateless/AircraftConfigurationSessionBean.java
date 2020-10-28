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
    public Long createAircraftConfiguration(AircraftConfiguration newAircraftConfig) {
        
        em.persist(newAircraftConfig);
        em.flush();
        
        return newAircraftConfig.getAircraftConfigId();
    }
    
    @Override
    public List<AircraftConfiguration> viewAllAircraftConfigs() {
        
        TypedQuery<AircraftConfiguration> query = em.createQuery("SELECT a FROM AircraftConfiguration a", AircraftConfiguration.class);
        List<AircraftConfiguration> aircraftConfigs = query.getResultList();
        
        return aircraftConfigs;
    }

    @Override
    public AircraftConfiguration viewAircraftConfig(String name) {
        
        Query query = em.createQuery("SELECT a FROM AircraftConfiguration a where a.aircraftConfigName = :aircraftConfigName")
                .setParameter("aircraftConfigName", name);
        // try catch
        AircraftConfiguration ac = (AircraftConfiguration) query.getSingleResult();
        
        return ac;
    }
    
}
