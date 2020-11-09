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
public class NumberOfSeatsPerRowMismatchException extends Exception {

    /**
     * Creates a new instance of
     * <code>NumberOfSeatsPerRowMismatchException</code> without detail message.
     */
    public NumberOfSeatsPerRowMismatchException() {
    }

    /**
     * Constructs an instance of
     * <code>NumberOfSeatsPerRowMismatchException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NumberOfSeatsPerRowMismatchException(String msg) {
        super(msg);
    }
}
