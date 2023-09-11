package com.intuit.urlshortner.repository;

import com.intuit.urlshortner.entities.IdRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IdRangeRepository extends JpaRepository<IdRange, Long> {

    @Query(value = "SELECT MAX(startId) FROM IdRange")
    Long findLastStartId();
}

