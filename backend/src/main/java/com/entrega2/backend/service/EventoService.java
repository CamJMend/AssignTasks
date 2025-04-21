package com.entrega2.backend.service;

import com.entrega2.backend.model.Evento;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class EventoService {

    private static final String COLLECTION = "eventos";

    public String save(Evento evento) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> result = db.collection(COLLECTION).document(evento.getId()).set(evento);
        return result.get().getUpdateTime().toString();
    }

    public List<Evento> getAll() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Evento> eventos = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            eventos.add(doc.toObject(Evento.class));
        }
        return eventos;
    }

    public Evento getById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot snapshot = db.collection(COLLECTION).document(id).get().get();
        return snapshot.exists() ? snapshot.toObject(Evento.class) : null;
    }

    public void delete(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }
}
