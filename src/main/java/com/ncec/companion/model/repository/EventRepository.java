package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.EventEntity;
import com.ncec.companion.model.enums.ParticipantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    @Query("select e from EventEntity e where e.startDate <= :endDate and e.endDate >= :startDate")
    Set<EventEntity> findAllEventsBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select e from EventEntity e where e.participantType = :participantType " +
            "or e.participantType = com.ncec.companion.model.enums.ParticipantType.ALL")
    Set<EventEntity> findAllByParticipantType(@Param("participantType") ParticipantType participantType);
}
