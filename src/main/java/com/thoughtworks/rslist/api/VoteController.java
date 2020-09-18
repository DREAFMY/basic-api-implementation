package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsService rsService;

    @GetMapping("/vote")
    public ResponseEntity<List<Vote>> should_get_record(@RequestParam int userId, @RequestParam int rsEventId) {
        List<Vote> all = voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId).stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .localDateTime(item.getLocalDateTime())
                        .rsEventId(item.getRsEvent().getId())
                        .voteNum(item.getVoteNum()).build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(all);
    }

    @PostMapping("/vote/{eventId}")
    public ResponseEntity vote_to_event(@RequestBody Vote vote, @RequestParam int eventId) {
        rsService.vote(vote, eventId);
        return ResponseEntity.ok().build();
    }


}
