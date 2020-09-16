package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void should_get_rs_event_list() throws Exception{
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_one_rs_event() throws Exception{
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条消息")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条消息")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条消息")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_rs_event_between() throws Exception{
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void should_add_rs_event() throws Exception {
        User user = new User("hahaha","male","123@a.com","18888888888",18);
        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rs() throws Exception {
        User user = new User("hahaha","male","123@a.com","18888888888",18);
        RsEvent rsEvent = new RsEvent("修改数据","修改数据关键字",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/change/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("修改数据")))
                .andExpect(jsonPath("$[1].keyWord", is("修改数据关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rs_name_null() throws Exception {
        User user = new User("hahaha","male","123@a.com","18888888888",18);
        RsEvent rsEvent = new RsEvent(null,"修改数据关键字",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/change/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("修改数据关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rs_key_word_null() throws Exception {
        User user = new User("hahaha","male","123@a.com","18888888888",18);
        RsEvent rsEvent = new RsEvent("修改数据",null,user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/change/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("修改数据")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_delete_rs() throws Exception {
        mockMvc.perform(delete("/rs/delete/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条消息")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第三条消息")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

}
