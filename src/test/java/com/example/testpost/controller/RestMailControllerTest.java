package com.example.testpost.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.testpost.entity.HistoryEvent;
import com.example.testpost.entity.PostOffice;
import com.example.testpost.entity.Shipment;
import com.example.testpost.service.HistoryEventService;
import com.example.testpost.service.PostOfficeService;
import com.example.testpost.service.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestMailControllerTest {

    private RestMailController restMailController;
    private ShipmentService shipmentService;
    private HistoryEventService historyEventService;
    private PostOfficeService postOfficeService;

    @BeforeEach
    public void setUp() {
        shipmentService = mock(ShipmentService.class);
        historyEventService = mock(HistoryEventService.class);
        postOfficeService = mock(PostOfficeService.class);
        restMailController = new RestMailController(shipmentService, historyEventService, postOfficeService);
    }

    @Test
    public void testCreateShipment_Success() {
        String recipientName = "John Doe";
        int shipmentType = 0; // PACKAGE
        int recipientZipCode = 12345;
        String recipientAddress = "1 Main St";
        Long postOfficeId = 1L;

        PostOffice postOffice = new PostOffice(1L, "Main Post Office", "someAddress");

        when(postOfficeService.findById(postOfficeId)).thenReturn(postOffice);

        ResponseEntity<Void> responseEntity = restMailController.createShipment(recipientName, shipmentType, recipientZipCode, recipientAddress, postOfficeId);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(shipmentService).saveShipment(any(Shipment.class));
        verify(historyEventService).createHistoryEvent(any(HistoryEvent.class));
    }

    @Test
    public void testArrivedShipmentStatus_Success() {
        Long shipmentId = 1L;
        Long postOfficeId = 2L;

        Shipment mockShipment = mock(Shipment.class);
        PostOffice postOffice = new PostOffice(postOfficeId, "Central Post Office", "anotherAddress");

        when(shipmentService.findById(shipmentId)).thenReturn(mockShipment);
        when(postOfficeService.findById(postOfficeId)).thenReturn(postOffice);

        restMailController.arrivedShipmentStatus(shipmentId, postOfficeId);

        verify(mockShipment).arrivedShipmentStatus();
        verify(shipmentService).saveShipment(any(Shipment.class));
        verify(historyEventService).createHistoryEvent(any(HistoryEvent.class));
    }

    @Test
    public void testLeftShipmentStatus_Success() {
        Long shipmentId = 2L;
        Long postOfficeId = 3L;
        Shipment mockShipment = mock(Shipment.class);

        PostOffice postOffice = new PostOffice(postOfficeId, "South Branch", "third Address");

        when(shipmentService.findById(shipmentId)).thenReturn(mockShipment);
        when(postOfficeService.findById(postOfficeId)).thenReturn(postOffice);

        restMailController.leftShipmentStatus(shipmentId, postOfficeId);


        verify(mockShipment).leftShipmentStatus();
        verify(shipmentService).saveShipment(any(Shipment.class));
        verify(historyEventService).createHistoryEvent(any(HistoryEvent.class));
    }

    @Test
    public void testGetShipment_Success() {
        Long shipmentId = 3L;
        Long postOfficeId = 1L;
        Shipment mockShipment = mock(Shipment.class);

        PostOffice postOffice = new PostOffice(postOfficeId, "Main Post Office", "someAddress");
        List<HistoryEvent> historyEvents = new ArrayList<>();
        historyEvents.add(new HistoryEvent(mockShipment, postOffice, new Date()));

        when(shipmentService.findById(shipmentId)).thenReturn(mockShipment);
        when(historyEventService.getShipmentHistory(shipmentId)).thenReturn(historyEvents);

        String response = restMailController.getShipment(shipmentId);

        assertEquals("Received successfully", response);
        verify(mockShipment).receiveShipmentStatus();
        verify(shipmentService).saveShipment(any(Shipment.class));
        verify(historyEventService).getShipmentHistory(shipmentId);
        verify(historyEventService).createHistoryEvent(any(HistoryEvent.class));
    }

    @Test
    public void testGetShipmentHistory_Success() {
        Long shipmentId = 1L;

        Shipment shipment = new Shipment(shipmentId, 3);

        List<HistoryEvent> historyEvents = new ArrayList<>();
        historyEvents.add(new HistoryEvent(shipment, new PostOffice(1L, "Main Post Office", "someAddress"), new Date()));
        historyEvents.add(new HistoryEvent(shipment, new PostOffice(2L, "Central Post Office", "anotherAddress"), new Date()));

        when(shipmentService.findById(shipmentId)).thenReturn(shipment);
        when(historyEventService.getShipmentHistory(shipmentId)).thenReturn(historyEvents);

        String response = restMailController.getShipmentHistory(shipmentId);

        assertTrue(response.contains("Shipment Status: " + shipment.getStatus()));
        for (HistoryEvent event : historyEvents) {
            String expectedEventString = String.format(
                    "  - Date: %s, Post Office: %s (%d), Status: %s\n",
                    event.getDate(), event.getPostOffice().getName(), event.getPostOffice().getIndex(), event.getShipmentStatus());
            assertTrue(response.contains(expectedEventString));
        }
    }
}