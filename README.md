# Perface

This is an simple library for identity the main node and the backup node in a two-machine cluster.

一个极其简单的类库，主要用于在双机集群下辨识主机及备机。借鉴了`RAFT`的思想，简化了`RAFT`后实现，不完整。

# Usage

## startup
```java
ABChecker abChecker = new ABChecker("127.0.0.1:22222;127.0.0.1:22223");
ABChecker abChecker = new ABChecker(new String[]{"127.0.0.1:22222", "127.0.0.1:22223"});
abChecker.init();
```

## identity
```java
boolean isMain = abChecker.isMain();
```

## shutdown
```java
abChecker.destroy();
```

# State Diagram

in plantuml format

```text
@startuml
hide empty description
state "从/Follower" as f
state "候选/Candidate" as c
state "主/Leader" as l
note top of f : 初始状态
f-->f:event:recv_req_vote\naction:1.vote for yes\n2.start new timeout\ndesc:等待周期内，收到投票请求，投票同意
f-->f:event:recv_ping\naction:start new timeout\ndesc:等待周期内，收到心跳
f-->c:event:timeout\naction:1.level up\n2.request for vote\ndesc:等待一个周期后自动提升，并发出投票请求
c-->f:event:recv_ping\naction:1.level down\n2.start new timeout\ndesc:等待周期内，收到心跳
c-->f:event:recv_req_vote\naction:1.level down\n2.vote for no\n3.start new timeout\ndesc:等待周期内，收到其他节点投票请求
c-->f:event:recv_vote(no)\naction:1.level down\n2.start new timeout\ndesc:等待周期内，收到其他节点投票，不同意
c-->l:event:recv_vote(yes)\naction:1.level up\n2.ping\n3.start new timeout\ndesc:等待周期内，收到其他节点投票，同意
c-->l:event:timeout\naction:1.level up\n2.ping\n3.start new timeout\ndesc:等待一个周期后自动提升
l-->l:event:timeout\naction:1.ping\n2.start new timeout\ndesc:等待一个周期后重新检查心跳
@enduml
```
