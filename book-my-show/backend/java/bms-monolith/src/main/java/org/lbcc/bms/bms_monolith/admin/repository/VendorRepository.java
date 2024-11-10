package org.lbcc.bms.bms_monolith.admin.repository;

import org.lbcc.bms.bms_monolith.common.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    //TODO: need to add indexing for name based search
    @Query("SELECT v FROM Vendor v WHERE v.name = :vendorName")
    List<Vendor> findByName(String vendorName);

    @Query("SELECT v FROM Vendor v WHERE v.name LIKE %:vendorName%")
    List<Vendor> findByNameContaining(String vendorName);

    @Query("SELECT v FROM Vendor v WHERE v.id = :vendorId")
    Optional<Vendor> findById(UUID vendorId);
}
