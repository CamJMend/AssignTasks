package com.entrega2.backend.service;

import com.entrega2.backend.model.Tarea;
import com.entrega2.backend.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AgenteService {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private WebSocketService webSocketService;
    
    // Método principal para asignar tareas automáticamente
    public void asignarTareasAutomaticamente() throws ExecutionException, InterruptedException {
        List<Tarea> tareasNoAsignadas = obtenerTareasNoAsignadas();
        List<Usuario> trabajadores = usuarioService.getAll();
        
        for (Tarea tarea : tareasNoAsignadas) {
            Usuario trabajadorElegido = aplicarReglas(tarea, trabajadores);
            
            if (trabajadorElegido != null) {
                // Asignar la tarea
                tarea.setAsignadoA(trabajadorElegido.getId());
                tareaService.save(tarea);
                
                // Actualizar contador de tareas del trabajador
                trabajadorElegido.incrementarTareas();
                usuarioService.save(trabajadorElegido);
                
                // Notificar por WebSocket
                String mensaje = "Nueva tarea asignada a " + trabajadorElegido.getNombre() + ": " + tarea.getDescripcion();
                webSocketService.enviarNotificacionTarea(mensaje);
            }
        }
    }
    
    // Obtener tareas no asignadas
    private List<Tarea> obtenerTareasNoAsignadas() throws ExecutionException, InterruptedException {
        List<Tarea> todasLasTareas = tareaService.getAll();
        return todasLasTareas.stream()
                .filter(tarea -> tarea.getAsignadoA() == null || tarea.getAsignadoA().isEmpty())
                .toList();
    }
    
    // Aplicar reglas para seleccionar trabajador
    private Usuario aplicarReglas(Tarea tarea, List<Usuario> trabajadores) {
        // Filtrar solo trabajadores disponibles (Regla 1)
        List<Usuario> disponibles = trabajadores.stream()
                .filter(Usuario::isDisponible)
                .toList();
        
        if (disponibles.isEmpty()) {
            return null;
        }
        
        // Regla 2: Si tarea es crítica, priorizar seniors
        if (tarea.isCritica()) {
            List<Usuario> seniors = disponibles.stream()
                    .filter(u -> "Senior".equalsIgnoreCase(u.getNivel()))
                    .toList();
            
            if (!seniors.isEmpty()) {
                // Regla 3: No asignar más de 4 tareas
                return seniors.stream()
                        .filter(u -> u.getTareasAsignadas() < 4)
                        .findFirst()
                        .orElse(null);
            }
        }
        
        // Regla 3: No asignar más de 4 tareas
        return disponibles.stream()
                .filter(u -> u.getTareasAsignadas() < 4)
                .findFirst()
                .orElse(null);
    }
    
    // Crear una tarea para testing
    public Tarea crearTareaEjemplo(String descripcion, boolean critica) throws ExecutionException, InterruptedException {
        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setId(UUID.randomUUID().toString());
        nuevaTarea.setDescripcion(descripcion);
        nuevaTarea.setCritica(critica);
        nuevaTarea.setFinalizada(false);
        
        tareaService.save(nuevaTarea);
        webSocketService.enviarNotificacionTarea("Nueva tarea creada: " + descripcion);
        
        return nuevaTarea;
    }
}