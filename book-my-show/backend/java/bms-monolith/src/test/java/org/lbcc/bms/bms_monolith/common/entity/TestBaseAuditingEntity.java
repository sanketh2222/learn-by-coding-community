package org.lbcc.bms.bms_monolith.common.entity;

import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestBaseAuditingEntity {
    private TestEntity testEntity;
    private UUID randomId;

    @BeforeEach
    void setUp() {
        randomId = UUID.randomUUID();
        testEntity = TestEntity.builder()
                .id(randomId)
                .createdDate(Instant.now())
                .lastModifiedDate(Instant.now())
                .createdBy("admin")
                .lastModifiedBy("admin")
                .build();
    }

    @Test
    void testBaseEntityFields() {
        // Verify the fields of the BaseEntity
        assertEquals(randomId, testEntity.getId());
        assertEquals("admin", testEntity.getCreatedBy());
        assertEquals("admin", testEntity.getLastModifiedBy());

        Instant createdDate = testEntity.getCreatedDate();
        Instant updatedDate = testEntity.getLastModifiedDate();

        // Verify createdDate and updatedDate are not null
        assertEquals(createdDate, testEntity.getCreatedDate());
        assertEquals(updatedDate, testEntity.getLastModifiedDate());
    }
}

@Entity
@SuperBuilder
class TestEntity extends BaseAuditingEntity {
    // You can add any additional fields specific to this test entity if needed
}

