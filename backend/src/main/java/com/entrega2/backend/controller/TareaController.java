package com.entrega2.backend.controller;

import com.entrega2.backend.model.Tarea;
import com.entrega2.backend.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tareas")
@CrossOrigin("*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping
    public String addTarea(@RequestBody Tarea tarea) throws ExecutionException, InterruptedException {
        return tareaService.save(tarea);
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
    public void deleteTarea(@PathVariable String id) {
        tareaService.delete(id);
    }

    @PutMapping("/{id}/completar")
    public void markTareaAsCompleted(@PathVariable String id) throws ExecutionException, InterruptedException {
        tareaService.markAsCompleted(id);
    }
}
