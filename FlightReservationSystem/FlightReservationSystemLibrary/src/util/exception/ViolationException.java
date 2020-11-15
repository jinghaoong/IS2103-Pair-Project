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
public class ViolationException extends Exception {

    /**
     * Creates a new instance of <code>ViolationException</code> without detail
     * message.
     */
    public ViolationException() {
    }

    /**
     * Constructs an instance of <code>ViolationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ViolationException(String msg) {
        super(msg);
    }
}
