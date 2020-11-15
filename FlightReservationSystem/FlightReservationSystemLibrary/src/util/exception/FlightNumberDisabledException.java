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
public class FlightNumberDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightNumberExistException</code> without
     * detail message.
     */
    public FlightNumberDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>FlightNumberExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightNumberDoesNotExistException(String msg) {
        super(msg);
    }
}
