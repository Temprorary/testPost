package com.example.testpost.service;

import com.example.testpost.entity.HistoryEvent;
import com.example.testpost.repository.HistoryEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryEventService {

    @Autowired
    private HistoryEventRepository historyEventRepository;

    public void createHistoryEvent(HistoryEvent historyEvent) {
        historyEventRepository.save(historyEvent);
    }

    public List<HistoryEvent> getShipmentHistory(Long id) {
        return historyEventRepository.getShipmentHistory(id);
    }
}
