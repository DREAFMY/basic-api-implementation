package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() { objectMapper = new ObjectMapper(); }

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user = new User("haha","male","2343@a.com","1888888888",33,10);
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserPO> all = userRepository.findAll();
       // assertEquals(1, all.size());
        assertEquals("haha", all.get(0).getName());

//        mockMvc.perform(get("/user"))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].name", is("hahaha")))
//                .andExpect(jsonPath("$[0].gender", is("male")))
//                .andExpect(jsonPath("$[0].email", is("123@a.com")))
//                .andExpect(jsonPath("$[0].phone", is("18888888888")))
//                .andExpect(jsonPath("$[0].age", is(18)))
//                .andExpect(jsonPath("$[0].voteNum", is(10)))
//                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void name_should_less_than_8() throws Exception {
        User user = new User("haha","male","2343@a.com","1888888888",33,10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("haha","male","2343@a.com","1888888888",33,10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void email_should_suit_format() throws Exception {
        User user = new User("haha","male","2343@a.com","1888888888",33,10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    public void phone_should_suit_format() throws Exception {
        User user = new User("haha","male","2343@a.com","1888888888",33,10);
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    public void should_get_user_by_id() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.userName",is("lulu")))
                .andExpect(jsonPath("$.email",is("123@a.com")))
                .andExpect(jsonPath("$.gender",is("female")))
                .andExpect(jsonPath("$.phone",is("18888888888")))
                .andExpect(jsonPath("$.age",is(13)))
                .andExpect(jsonPath("$.voteNum",is(10)))
                .andExpect(status().isOk());
    }
}
