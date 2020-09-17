package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;

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

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        Optional<UserPO> one = userRepository.findById(id);
        return ResponseEntity.ok(one.get());
    }

    @GetMapping("/user")
    public List<User> getUserList() {
        return userList;
    }

}
