package com.entrega2.backend.controller;

import com.entrega2.backend.model.Tarea;
import com.entrega2.backend.service.AgenteService;
import com.entrega2.backend.service.TareaService;
import com.entrega2.backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tareas")
@CrossOrigin("*")
public class TareaController {

    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private WebSocketService webSocketService;
    
    @Autowired
    private AgenteService agenteService;

    @PostMapping
    public String addTarea(@RequestBody Tarea tarea) throws ExecutionException, InterruptedException {
        if (tarea.getId() == null || tarea.getId().isEmpty()) {
            tarea.setId(UUID.randomUUID().toString());
        }
        String result = tareaService.save(tarea);

        webSocketService.enviarNotificacionTarea("Nueva tarea creada: " + tarea.getDescripcion());
        
        return result;
    }

    @GetMapping
    public List<Tarea> getTareas() throws ExecutionException, InterruptedException {
        return tareaService.getAll();
    }

    @GetMapping("/{id}")
    public Tarea getTarea(@PathVariable String id) throws ExecutionException, InterruptedException {
        return tareaService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTarea(@PathVariable String id) throws ExecutionException, InterruptedException {
        Tarea tarea = tareaService.getById(id); 
        if (tarea != null) {
            tareaService.delete(id);
            webSocketService.enviarNotificacionTarea("Tarea eliminada: " + tarea.getDescripcion());
        }
    }

    @PutMapping("/{id}/completar")
    public void markTareaAsCompleted(@PathVariable String id) throws ExecutionException, InterruptedException {
        Tarea tarea = tareaService.getById(id);
        if (tarea != null) {
            tareaService.markAsCompleted(id);
            webSocketService.enviarNotificacionTarea("Tarea completada: " + tarea.getDescripcion());
        }
    }
    
    @PostMapping("/asignar-automaticamente")
    public String asignarTareasAutomaticamente() throws ExecutionException, InterruptedException {
        agenteService.asignarTareasAutomaticamente();
        return "Asignación automática completada";
    }
    
    @PostMapping("/crear-ejemplo")
    public Tarea crearTareaEjemplo(@RequestParam String descripcion, @RequestParam boolean critica) 
            throws ExecutionException, InterruptedException {
        return agenteService.crearTareaEjemplo(descripcion, critica);
    }
}