package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private int age;
    private int voteNum;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;
}
