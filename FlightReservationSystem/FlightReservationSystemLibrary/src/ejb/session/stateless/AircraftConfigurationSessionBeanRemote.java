/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author yylow
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {

    public AircraftConfiguration createAircraftConfiguration(AircraftConfiguration newAircraftConfig);

    public List<AircraftConfiguration> retrieveAllAircraftConfigs();

    public AircraftConfiguration viewAircraftConfig(String name);
    
}
