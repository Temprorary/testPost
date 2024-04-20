package com.example.testpost.repository;

import com.example.testpost.entity.HistoryEvent;
import com.example.testpost.entity.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryEventRepository extends JpaRepository<HistoryEvent, Integer>{
    @Query("SELECT h FROM HistoryEvent h WHERE h.shipment.id = :shipmentId")
    List<HistoryEvent> getShipmentHistory(Long shipmentId);
}