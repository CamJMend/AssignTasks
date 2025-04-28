import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { subscribeTareas } from '../services/websocketService';

const WorkerDashboard = ({ userId }) => {
  const [tareas, setTareas] = useState([]);
  const [notificaciones, setNotificaciones] = useState([]);
  const [usuario, setUsuario] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    // Cargar datos del usuario
    axios.get(`http://localhost:8080/usuarios/${userId}`)
      .then(response => {
        console.log("Datos de usuario cargados:", response.data);
        setUsuario(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error al cargar usuario:', error);
        setError("No se pudo cargar la información del usuario");
        setLoading(false);
      });
    
    // Cargar tareas asignadas
    cargarTareas();
    
    // Suscribirse a notificaciones de tareas
    const unsubscribe = subscribeTareas((mensaje) => {
      console.log("Notificación recibida:", mensaje);
      setNotificaciones(prev => [...prev, mensaje]);
      // Recargar tareas cuando llegue una notificación
      cargarTareas();
    });
    
    return () => {
      unsubscribe();
    };
  }, [userId]);
  
  const cargarTareas = () => {
    axios.get('http://localhost:8080/tareas')
      .then(response => {
        console.log("Tareas cargadas:", response.data);
        // Filtrar solo las tareas asignadas a este usuario
        const misTareas = response.data.filter(tarea => tarea.asignadoA === userId);
        setTareas(misTareas);
      })
      .catch(error => {
        console.error('Error al cargar tareas:', error);
        setError("No se pudieron cargar las tareas");
      });
  };
  
  const marcarComoCompletada = (tareaId) => {
    axios.put(`http://localhost:8080/tareas/${tareaId}/completar`)
      .then(() => {
        console.log("Tarea marcada como completada:", tareaId);
        cargarTareas();
      })
      .catch(error => {
        console.error('Error al completar tarea:', error);
      });
  };
  
  if (loading) {
    return <div className="loading">Cargando...</div>;
  }
  
  if (error) {
    return (
      <div className="error-container">
        <h2>Error</h2>
        <p>{error}</p>
        <button onClick={() => window.location.reload()}>Reintentar</button>
      </div>
    );
  }
  
  if (!usuario) {
    return <div className="error-container">No se pudo cargar el usuario</div>;
  }
  
  return (
    <div className="worker-dashboard">
      <h1>Dashboard de {usuario.nombre}</h1>
      <h2>Nivel: {usuario.nivel}</h2>
      <h3>Estado: {usuario.disponible ? 'Disponible' : 'No disponible'}</h3>
      
      <div className="tareas-container">
        <h2>Mis Tareas</h2>
        {tareas.length === 0 ? (
          <p>No tienes tareas asignadas.</p>
        ) : (
          <ul className="tareas-list">
            {tareas.map(tarea => (
              <li key={tarea.id} className={`tarea-item ${tarea.finalizada ? 'completed' : ''}`}>
                <div className="tarea-info">
                  <h3>{tarea.descripcion}</h3>
                  <p>Crítica: {tarea.critica ? 'Sí' : 'No'}</p>
                  <p>Estado: {tarea.finalizada ? 'Completada' : 'Pendiente'}</p>
                </div>
                {!tarea.finalizada && (
                  <button 
                    className="complete-btn"
                    onClick={() => marcarComoCompletada(tarea.id)}
                  >
                    Marcar como completada
                  </button>
                )}
              </li>
            ))}
          </ul>
        )}
      </div>
      
      <div className="notificaciones">
        <h2>Notificaciones</h2>
        {notificaciones.length === 0 ? (
          <p>No hay notificaciones recientes.</p>
        ) : (
          <ul className="notificaciones-list">
            {notificaciones.map((notificacion, index) => (
              <li key={index} className="notificacion-item">{notificacion}</li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default WorkerDashboard;