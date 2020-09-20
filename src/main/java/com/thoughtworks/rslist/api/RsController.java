package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEventList();

  private List<RsEvent> initRsEventList() {
    User user = new User("hahaha","male","123@a.com","18888888888",18);
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("第一条消息", "无关键字",user));
    rsEventList.add(new RsEvent("第二条消息", "无关键字",user));
    rsEventList.add(new RsEvent("第三条消息", "无关键字",user));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRsEvent(@PathVariable int index) {
    rsList = initRsEventList();
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/event")
  // @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
    rsList = initRsEventList();
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    rsList.add(event);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/change/{index}")
  public ResponseEntity changeRsEvent(@RequestBody String rsEvent, @PathVariable int index) throws JsonProcessingException {
    rsList = initRsEventList();

    HttpHeaders headers = new HttpHeaders();
    headers.add("index", index+"");

    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    RsEvent re = rsList.get(index - 1);
    if (event.getEventName() != null) {
      re.setEventName(event.getEventName());
    }
    if (event.getKeyWord() != null) {
      re.setKeyWord(event.getKeyWord());
    }
    rsList.set(index - 1, re);
    return ResponseEntity.created(null).headers(headers).build();
  }

  @DeleteMapping("/rs/delete/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
    rsList = initRsEventList();
    rsList.remove(index - 1);
    return ResponseEntity.ok().build();
  }

}
