package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setGender(user.getGender());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user")
    public ResponseEntity getUserList() {
        List<UserPO> all = userRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity delete_user_and_rsEvent(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity get_user_by_id(@PathVariable int id) {
        Optional<UserPO> user = userRepository.findById(id);
        return ResponseEntity.ok(user.get());
    }

}
