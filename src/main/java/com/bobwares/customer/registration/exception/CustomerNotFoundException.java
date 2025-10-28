/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.exception
 * File: CustomerNotFoundException.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI
 * Date: 2025-10-28T15:32:47Z
 * Exports: CustomerNotFoundException
 * Description: Domain-specific runtime exception thrown when a customer cannot be located by identifier.
 */
package com.bobwares.customer.registration.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(UUID id) {
        super("Customer not found for id: " + id);
    }
}
