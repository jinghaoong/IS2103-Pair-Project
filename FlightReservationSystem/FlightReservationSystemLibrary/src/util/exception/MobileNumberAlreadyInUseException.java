/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author yylow
 */
public class MobileNumberAlreadyInUseException extends Exception {

    /**
     * Creates a new instance of <code>MobileNumberAlreadyInUse</code> without
     * detail message.
     */
    public MobileNumberAlreadyInUseException() {
    }

    /**
     * Constructs an instance of <code>MobileNumberAlreadyInUse</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MobileNumberAlreadyInUseException(String msg) {
        super(msg);
    }
}
