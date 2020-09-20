package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePO, Integer> {
    @Override
    List<VotePO> findAll();
    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId);

    @Query("select v from vote v where v.localDateTime >= :startTime and v.localDateTime <= :endTime")
    List<VotePO> myLocalDateTime(String startTime, String endTime);
}
