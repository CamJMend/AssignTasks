package com.entrega2.backend.controller;

import com.entrega2.backend.model.Evento;
import com.entrega2.backend.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/eventos")
@CrossOrigin("*")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    public String addEvento(@RequestBody Evento evento) throws ExecutionException, InterruptedException {
        return eventoService.save(evento);
    }

    @GetMapping
    public List<Evento> getEventos() throws ExecutionException, InterruptedException {
        return eventoService.getAll();
    }

    @GetMapping("/{id}")
    public Evento getEvento(@PathVariable String id) throws ExecutionException, InterruptedException {
        return eventoService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable String id) {
        eventoService.delete(id);
    }
}
