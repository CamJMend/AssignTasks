package com.entrega2.backend.service;

import com.entrega2.backend.model.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UsuarioService {

    private static final String COLLECTION = "usuarios";

    public String save(Usuario usuario) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> result = db.collection(COLLECTION).document(usuario.getId()).set(usuario);
        return result.get().getUpdateTime().toString();
    }

    public List<Usuario> getAll() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Usuario> usuarios = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            usuarios.add(doc.toObject(Usuario.class));
        }
        return usuarios;
    }

    public Usuario getById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot snapshot = db.collection(COLLECTION).document(id).get().get();
        return snapshot.exists() ? snapshot.toObject(Usuario.class) : null;
    }

    public void delete(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }
}
