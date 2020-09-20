package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

import java.util.ArrayList;
import java.util.List;

public class RsList {
    public static List<RsEvent> rsList = new ArrayList<RsEvent>() {{
        add(new RsEvent("第一条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
        add(new RsEvent("第二条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
        add(new RsEvent("第三条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
    }};

    public void reSetList() {
        rsList.set(0,new RsEvent("第一条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
        rsList.set(1,new RsEvent("第二条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
        rsList.set(2,new RsEvent("第二条消息", "无关键字",new User("hahaha","male","123@a.com","18888888888",18)));
    }
}
