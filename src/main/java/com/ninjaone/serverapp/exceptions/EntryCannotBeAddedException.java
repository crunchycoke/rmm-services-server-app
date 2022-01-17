package com.ninjaone.serverapp.exceptions;

/**
 * Thrown when the provided entry cannot be added to the database.
 */
public class EntryCannotBeAddedException extends RuntimeException {

    public EntryCannotBeAddedException(Object entry) {
        super("Entry '" + entry + "' cannot be added");
    }

    public EntryCannotBeAddedException(Object entry, Throwable err) {
        super("Entry '" + entry + "' cannot be added: " + err.getLocalizedMessage(), err);
    }
}