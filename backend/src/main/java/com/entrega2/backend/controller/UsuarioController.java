package com.entrega2.backend.controller;

import com.entrega2.backend.model.Usuario;
import com.entrega2.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public String addUsuario(@RequestBody Usuario usuario) throws ExecutionException, InterruptedException {
        return usuarioService.save(usuario);
    }

    @GetMapping
    public List<Usuario> getUsuarios() throws ExecutionException, InterruptedException {
        return usuarioService.getAll();
    }

    @GetMapping("/{id}")
    public Usuario getUsuario(@PathVariable String id) throws ExecutionException, InterruptedException {
        return usuarioService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable String id) {
        usuarioService.delete(id);
    }
}
