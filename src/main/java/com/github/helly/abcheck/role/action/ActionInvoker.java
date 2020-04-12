package com.github.helly.abcheck.role.action;

import com.github.helly.abcheck.ABCommander;

import java.util.EventObject;

/**
 * @author Helly Guo
 * <p>
 * Created on 4/12/20 3:54 PM
 */
public interface ActionInvoker {
    void invoke(ABCommander commander, EventObject event);
}
