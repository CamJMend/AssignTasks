package com.entrega2.backend.service;

import com.entrega2.backend.model.Tarea;
import com.entrega2.backend.model.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class TareaService {

    private static final String COLLECTION = "tareas";

    public String save(Tarea tarea) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> result = db.collection(COLLECTION).document(tarea.getId()).set(tarea);
        return result.get().getUpdateTime().toString();
    }

    public List<Tarea> getAll() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Tarea> tareas = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            tareas.add(doc.toObject(Tarea.class));
        }
        return tareas;
    }

    public Tarea getById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot snapshot = db.collection(COLLECTION).document(id).get().get();
        return snapshot.exists() ? snapshot.toObject(Tarea.class) : null;
    }

    public void delete(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }

    public void markAsCompleted(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference tareaDocRef = db.collection(COLLECTION).document(id);
        Tarea tarea = tareaDocRef.get().get().toObject(Tarea.class);

        if (tarea != null && tarea.getAsignadoA() != null) {
            ApiFuture<WriteResult> tareaUpdate = tareaDocRef.update("finalizada", true);
            tareaUpdate.get();
            
            DocumentReference usuarioDocRef = db.collection("usuarios").document(tarea.getAsignadoA());
            ApiFuture<DocumentSnapshot> usuarioSnapshot = usuarioDocRef.get();
            DocumentSnapshot snapshot = usuarioSnapshot.get();
            
            if (snapshot.exists()) {
                Usuario usuario = snapshot.toObject(Usuario.class);
                if (usuario != null) {
                    usuario.decrementarTareas();
                    ApiFuture<WriteResult> usuarioUpdate = usuarioDocRef.update("tareasAsignadas", usuario.getTareasAsignadas());
                    usuarioUpdate.get();
                }
            }
        }
    }

}
