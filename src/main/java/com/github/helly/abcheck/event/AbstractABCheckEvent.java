package com.github.helly.abcheck.event;

import com.github.helly.abcheck.sock.ReqPackage;

import java.util.EventObject;

/**
 * 抽象事件
 *
 * @author HellyGuo
 */
abstract class AbstractABCheckEvent extends EventObject implements ABCheckEvent {

    private static final Object FAKE_SOURCE = new Object();

    public AbstractABCheckEvent() {
        this(FAKE_SOURCE);
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AbstractABCheckEvent(Object source) {
        super(source);
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AbstractABCheckEvent(ReqPackage source) {
        super(source);
    }
}
