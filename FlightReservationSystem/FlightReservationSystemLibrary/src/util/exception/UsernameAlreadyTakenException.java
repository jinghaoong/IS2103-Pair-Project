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
public class UsernameAlreadyTakenException extends Exception {

    /**
     * Creates a new instance of <code>UsernameAlreadyTakenException</code>
     * without detail message.
     */
    public UsernameAlreadyTakenException() {
    }

    /**
     * Constructs an instance of <code>UsernameAlreadyTakenException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UsernameAlreadyTakenException(String msg) {
        super(msg);
    }
}
