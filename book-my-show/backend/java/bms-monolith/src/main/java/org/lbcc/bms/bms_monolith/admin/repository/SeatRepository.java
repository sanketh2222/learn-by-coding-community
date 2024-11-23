package org.lbcc.bms.bms_monolith.admin.repository;

import org.lbcc.bms.bms_monolith.common.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

}
