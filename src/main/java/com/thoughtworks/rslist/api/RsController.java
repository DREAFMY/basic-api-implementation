package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

  RsEventRepository rsEventRepository;
  UserRepository userRepository;

  public RsController( RsEventRepository rsEventRepository, UserRepository userRepository) {
    this.rsEventRepository = rsEventRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) {
    if (index < 1 || index > rsEventRepository.findAll().size()) {
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsEventRepository.findAll().get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEventPO>> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    List<RsEventPO> all = rsEventRepository.findAll();
    if (start == null || end == null) {
      return ResponseEntity.ok(rsEventRepository.findAll());
    }
    return ResponseEntity.ok(rsEventRepository.findAll().subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEventPO rsEventPO) {
    Optional<UserPO> userPO = userRepository.findById(rsEventPO.getUserPO().getId());
    if (!userPO.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    rsEventRepository.save(rsEventPO);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/change/{userId}")
  public ResponseEntity changeRsEvent(@RequestBody RsEventPO rsEventPO, @PathVariable int userId) throws Exception {
    Optional<UserPO> user = userRepository.findById(userId);
    if (!user.isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPO temp = rsEventRepository.findById(rsEventPO.getId()).get();
    if (rsEventPO.getEventName() != null) {
      temp.setEventName(rsEventPO.getEventName());
    }
    if (rsEventPO.getKeyWord() != null) {
      temp.setKeyWord(rsEventPO.getKeyWord());
    }
    rsEventRepository.save(temp);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/delete/{id}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int id) {
    rsEventRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }


}
