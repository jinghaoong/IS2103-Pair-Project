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
public class FlightScheduleOverlapException extends Exception {

    /**
     * Creates a new instance of <code>FlightScheduleOverlapException</code>
     * without detail message.
     */
    public FlightScheduleOverlapException() {
    }

    /**
     * Constructs an instance of <code>FlightScheduleOverlapException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightScheduleOverlapException(String msg) {
        super(msg);
    }
}
