package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    UserPO userPO;
    RsEventPO rsEventPO;
    VotePO votePO;

    @BeforeEach
    void setUp() {
        userPO = userRepository.save(UserPO.builder().email("adsfaf@b.com").age(45).gender("female").phone("18888888888").name("asffnn").voteNum(10).build());
        rsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("股市").keyWord("经济").userPO(userPO).voteNum(0).build());
    }

    @AfterEach
    void cleanPlat() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void should_get_vote_record() throws Exception {
        votePO = voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(5).user(userPO).localDateTime(LocalDateTime.now()).build());
        mockMvc.perform(get("/vote")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));

    }

    @Test
    public void test_mockMvc_is_ok() throws Exception {
        Vote vote = Vote.builder().voteNum(5).rsEventId(rsEventPO.getId()).userId(userPO.getId()).localDateTime(LocalDateTime.now()).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/{id}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}