package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @AfterEach
    void cleanPlat() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_get_rs_event_list() throws Exception{
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("股市蹦了").keyWord("经济").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventRepository.save(rsEventPO1);
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("股市蹦了")))
                .andExpect(jsonPath("$[1].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_one_rs_event_by_id() throws Exception{
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("股市蹦了").keyWord("经济").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventRepository.save(rsEventPO1);
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$.keyWord", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("股市蹦了")))
                .andExpect(jsonPath("$.keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_rs_event_between() throws Exception{
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("股市蹦了").keyWord("经济").userPO(savedUser).build();
        RsEventPO rsEventPO2 = RsEventPO.builder().eventName("开学的第一天").keyWord("学习").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventRepository.save(rsEventPO1);
        rsEventRepository.save(rsEventPO2);
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("股市蹦了")))
                .andExpect(jsonPath("$[1].keyWord", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("股市蹦了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("开学的第一天")))
                .andExpect(jsonPath("$[1].keyWord", is("学习")))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void should_add_rs_event_when_user_exist() throws Exception {
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEventPO);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals("猪肉涨价了",all.get(0).getEventName());
        assertEquals("经济",all.get(0).getKeyWord());
    }


    @Test
    public void should_change_rs_when_have_use() throws Exception {
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventPO.setEventName("修改猪肉涨价了");
        rsEventPO.setKeyWord("修改经济");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEventPO);

        mockMvc.perform(patch("/rs/change/{userId}",savedUser.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("修改猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("修改经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_change_rs_name_null() throws Exception {
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventPO.setEventName(null);
        rsEventPO.setKeyWord("修改经济");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEventPO);

        mockMvc.perform(patch("/rs/change/{userId}",savedUser.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord", is("修改经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_delete_rs() throws Exception {
        UserPO savedUser = userRepository.save(UserPO.builder().name("lulu").age(23).phone("18888888888").email("123@a.com").gender("female").voteNum(10).build());
        RsEventPO rsEventPO = RsEventPO.builder().eventName("猪肉涨价了").keyWord("经济").userPO(savedUser).build();
        RsEventPO rsEventPO1 = RsEventPO.builder().eventName("股市蹦了").keyWord("经济").userPO(savedUser).build();
        rsEventRepository.save(rsEventPO);
        rsEventRepository.save(rsEventPO1);

        mockMvc.perform(delete("/rs/delete/{id}", rsEventPO.getId())).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("股市蹦了")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_rs_event_not_valid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

//    @Test
//    public void should_throw_method_argument_not_valid_exception() throws Exception {
////        User user = new User("hahahahahahhaha","male","123@a.com","18888888888",18);
//        RsEvent rsEvent = new RsEvent("猪肉涨价了","经济",1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error",is("invalid param")));
//    }

}
