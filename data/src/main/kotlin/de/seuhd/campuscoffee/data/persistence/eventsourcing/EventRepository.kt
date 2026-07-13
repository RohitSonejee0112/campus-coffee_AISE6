package de.seuhd.campuscoffee.data.persistence.eventsourcing

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Repository for the append-only event log.
 */
interface EventRepository : JpaRepository<EventEntity, UUID> {
    /** All events in append order (by the monotonic [EventEntity.seq]), for replaying the whole log. */
    fun findAllByOrderBySeqAsc(): List<EventEntity>

    /**
     * Finds all events for a given entity ID, ordered by sequence number descending.
     * The entity ID is stored inside the JSONB body.
     *
     * @param entityId the ID of the entity to retrieve events for
     */
    @org.springframework.data.jpa.repository.Query(
        value = "SELECT * FROM events WHERE body->>'id' = :entityId ORDER BY seq DESC",
        nativeQuery = true
    )
    fun findByEntityIdOrderBySeqDesc(
        @org.springframework.data.repository.query.Param("entityId") entityId: String
    ): List<EventEntity>

    /**
     * Whether the log already holds at least one event for the given domain type, so the import can skip it.
     *
     * @param entityType the entity type label (the domain class's simple name)
     */
    fun existsByEntityType(entityType: String): Boolean

    /**
     * Removes every event for the given domain type, when clearing that type's data.
     *
     * @param entityType the entity type label (the domain class's simple name)
     */
    fun deleteByEntityType(entityType: String)
}
