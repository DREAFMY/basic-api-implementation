package com.thoughtworks.rslist.po;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotePO {
    @Id
    @GeneratedValue
    private int id;
    private int voteNum;
    private LocalDateTime localDateTime;

    @ManyToOne
    @JsonIgnore
    private RsEventPO rsEvent;

    @ManyToOne
    @JsonIgnore
    private UserPO user;
}
