package com.soumya.ipTracker20.repository;

import com.soumya.ipTracker20.entity.IpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpLogRepository extends JpaRepository<IpLog , Integer> {
    Optional<IpLog> findTopByOrderBySnoDesc();
    List<IpLog> findByUsernameOrderBySnoDesc(String username);
}
