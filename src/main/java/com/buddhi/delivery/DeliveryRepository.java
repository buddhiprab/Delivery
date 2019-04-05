package com.buddhi.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByPickupName(String name);

    @Query("SELECT d FROM Delivery d WHERE d.dropAddress=:dropAddress")
    public List<Delivery> fetchDeliveryByDropAddress(@Param("dropAddress") String dropAddress);
}
