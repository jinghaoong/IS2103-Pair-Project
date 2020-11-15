/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Remote;
import util.exception.ViolationException;

/**
 *
 * @author jinghao
 */
@Remote
public interface ValidatorSessionBeanRemote {
    public void validate(Object object) throws ViolationException;
}
