package org.lbcc.bms.bms_monolith.eventservice.repository;

import org.lbcc.bms.bms_monolith.common.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findAll(Pageable pageable);
}