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
import java.time.format.DateTimeFormatter;

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
        votePO = voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(5).user(userPO).localDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))).build());

        mockMvc.perform(get("/vote")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));

    }

    @Test
    public void should_vote_success() throws Exception {
        Vote vote = Vote.builder().voteNum(5).rsEventId(rsEventPO.getId()).userId(userPO.getId()).localDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/{id}",rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_vote_between_start_and_end_time() throws Exception{
        voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(1).user(userPO).localDateTime(LocalDateTime.of(2020, 8, 10, 3, 30, 10, 000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))).build());
        voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(2).user(userPO).localDateTime(LocalDateTime.of(2020, 9, 11, 3, 30, 10, 000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))).build());
        voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(3).user(userPO).localDateTime(LocalDateTime.of(2020, 10, 12, 3, 30, 10, 000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))).build());

        mockMvc.perform(get("/vote")
                .param("startTime", "2020-09-11 00:00:00.000")
                .param("endTime", "2020-09-11 23:59:59.000"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].localDateTime", is("2020-09-11 03:30:10.000")))
                .andExpect(jsonPath("$[0].voteNum", is(2)))
                .andExpect(status().isOk());
    }

}