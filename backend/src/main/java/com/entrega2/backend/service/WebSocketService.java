package com.entrega2.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacionTarea(String mensaje) {
        messagingTemplate.convertAndSend("/topic/tareas", mensaje);
    }
    
    public void enviarActualizacionUsuarios(String mensaje) {
        messagingTemplate.convertAndSend("/topic/usuarios", mensaje);
    }
    
    public void enviarActualizacionEventos(String mensaje) {
        messagingTemplate.convertAndSend("/topic/eventos", mensaje);
    }
}