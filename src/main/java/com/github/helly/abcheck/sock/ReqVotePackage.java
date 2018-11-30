package com.github.helly.abcheck.sock;

/**
 * 请求投票结构
 *
 * @author HellyGuo
 */
public class ReqVotePackage extends BaseReqPackage {
    public ReqVotePackage() {
        super(PackageType.REQ_VOTE);
    }

    public ReqVotePackage(long version) {
        super(PackageType.REQ_VOTE, version);
    }
}
