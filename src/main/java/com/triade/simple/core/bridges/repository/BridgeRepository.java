package com.triade.simple.core.bridges.repository;

import com.triade.simple.core.bridges.domain.Bridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BridgeRepository extends JpaRepository<Bridge, Long> {
}
