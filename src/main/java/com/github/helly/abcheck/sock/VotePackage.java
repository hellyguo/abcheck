package com.github.helly.abcheck.sock;

import static com.github.helly.abcheck.constant.ABCheckConstants.NO_VAL;
import static com.github.helly.abcheck.constant.ABCheckConstants.YES_VAL;

/**
 * 投票结果结构
 *
 * @author HellyGuo
 */
public class VotePackage extends BaseReqPackage {

    public VotePackage() {
        super(PackageType.VOTE);
    }

    public VotePackage(boolean yes) {
        this();
        this.version = yes ? YES_VAL : NO_VAL;
    }

    public boolean yes() {
        return version > 0;
    }
}
