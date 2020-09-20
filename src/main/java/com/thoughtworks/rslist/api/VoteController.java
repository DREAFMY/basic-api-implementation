package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsService rsService;

    @PostMapping("/vote/{id}")
    public ResponseEntity vote_to_event(@PathVariable int id, @RequestBody Vote vote) {
        rsService.vote(vote, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vote")
    public ResponseEntity<List<Vote>> should_get_record(@RequestParam(required = false) Integer userId, @RequestParam(required = false) Integer rsEventId, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        List<VotePO> allPO = new ArrayList<>();
        List<Vote> all;
        if (startTime != null && endTime != null) {
            System.out.println("***************************");
           // allPO = voteRepository.findAllBetweenStartTimeAndEndTime(startTime, endTime);
        } else {
            allPO = voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId);
        }
         all = allPO.stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .localDateTime(item.getLocalDateTime())
                        .rsEventId(item.getRsEvent().getId())
                        .voteNum(item.getVoteNum()).build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(all);
    }

}
