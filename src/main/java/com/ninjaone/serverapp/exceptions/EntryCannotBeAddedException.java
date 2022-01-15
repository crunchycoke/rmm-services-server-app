package com.ninjaone.serverapp.exceptions;

public class EntryCannotBeAddedException extends RuntimeException {

    public EntryCannotBeAddedException(Object entry, Throwable err) {
        super("Entry '" + entry + "' cannot be added: " + err.getLocalizedMessage(), err);
    }
}