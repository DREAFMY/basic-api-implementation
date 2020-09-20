package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEventList();

  private List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("第一条消息", "无关键字"));
    rsEventList.add(new RsEvent("第二条消息", "无关键字"));
    rsEventList.add(new RsEvent("第三条消息", "无关键字"));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    rsList = initRsEventList();
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
    rsList = initRsEventList();
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    rsList.add(event);
  }

  @PostMapping("/rs/change/{index}")
  public void changeRsEvent(@RequestBody String rsEvent, @PathVariable int index) throws JsonProcessingException {
    rsList = initRsEventList();
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
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteOneRsEvent(@PathVariable int index) {
    rsList = initRsEventList();
    rsList.remove(index - 1);
  }

}
