package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface RsEventRepository extends CrudRepository<RsEventPO, Integer> {
    @Override
    List<RsEventPO> findAll();
}
