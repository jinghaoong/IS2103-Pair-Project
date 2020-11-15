/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Set;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ViolationException;

/**
 *
 * @author jinghao
 */
@Stateless
public class ValidatorSessionBean implements ValidatorSessionBeanRemote, ValidatorSessionBeanLocal {
    
    public void validate(Object object) throws ViolationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            String violationMessages = "";
            for (ConstraintViolation<Object> violation : violations) {
                violationMessages += "\n" + violation.getMessage();
            }
            throw new ViolationException(violationMessages);
        }
    }
}
