package com.github.helly.abcheck.event;

/**
 * @author Helly Guo
 * <p>
 * Created on 4/12/20 1:44 PM
 */
public interface EventCreator {
    ABCheckEvent create(int type, long version);
}
