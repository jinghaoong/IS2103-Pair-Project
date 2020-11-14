/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author jinghao
 */
public class FlightAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightAlreadyExistException</code>
     * without detail message.
     */
    public FlightAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>FlightAlreadyExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightAlreadyExistException(String msg) {
        super(msg);
    }
}
