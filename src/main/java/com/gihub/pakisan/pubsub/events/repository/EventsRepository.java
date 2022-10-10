package com.gihub.pakisan.pubsub.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface EventsRepository extends JpaRepository<Event, UUID> {

    @Nonnull
    List<Event> findAllByType(@Nonnull EventType eventType);

}
