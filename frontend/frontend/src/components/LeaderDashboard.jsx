import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { subscribeTareas, subscribeUsuarios } from '../services/websocketService';

const LeaderDashboard = () => {
  const [tareas, setTareas] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [notificaciones, setNotificaciones] = useState([]);
  const [nuevaTarea, setNuevaTarea] = useState({
    descripcion: '',
    critica: false
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    // Cargar datos iniciales
    Promise.all([
      cargarTareas(),
      cargarUsuarios()
    ])
      .then(() => setLoading(false))
      .catch(err => {
        console.error("Error al cargar datos iniciales:", err);
        setError("No se pudieron cargar los datos. Verifica la conexión con el backend.");
        setLoading(false);
      });
    
    // Suscribirse a notificaciones
    const unsubscribeTareas = subscribeTareas((mensaje) => {
      console.log("Notificación de tarea recibida:", mensaje);
      setNotificaciones(prev => [...prev, mensaje]);
      cargarTareas();
    });
    
    const unsubscribeUsuarios = subscribeUsuarios(() => {
      console.log("Notificación de usuario recibida");
      cargarUsuarios();
    });
    
    return () => {
      unsubscribeTareas();
      unsubscribeUsuarios();
    };
  }, []);
  
  const cargarTareas = () => {
    return axios.get('http://localhost:8080/tareas')
      .then(response => {
        console.log("Tareas cargadas:", response.data);
        setTareas(response.data);
      })
      .catch(error => {
        console.error('Error al cargar tareas:', error);
        throw error;
      });
  };
  
  const cargarUsuarios = () => {
    return axios.get('http://localhost:8080/usuarios')
      .then(response => {
        console.log("Usuarios cargados:", response.data);
        setUsuarios(response.data);
      })
      .catch(error => {
        console.error('Error al cargar usuarios:', error);
        throw error;
      });
  };
  
  const ejecutarAgente = () => {
    axios.post('http://localhost:8080/tareas/asignar-automaticamente')
      .then(() => {
        console.log("Agente ejecutado con éxito");
        alert('Asignación automática completada');
        cargarTareas();
        cargarUsuarios();
      })
      .catch(error => {
        console.error('Error al ejecutar agente:', error);
        alert('Error al ejecutar el agente de asignación');
      });
  };
  
  const crearNuevaTarea = (e) => {
    e.preventDefault();
    axios.post('http://localhost:8080/tareas', {
      descripcion: nuevaTarea.descripcion,
      critica: nuevaTarea.critica,
      finalizada: false
    })
      .then(() => {
        console.log("Tarea creada con éxito");
        setNuevaTarea({ descripcion: '', critica: false });
        cargarTareas();
      })
      .catch(error => {
        console.error('Error al crear tarea:', error);
        alert('Error al crear la tarea');
      });
  };
  
  const cambiarEstadoUsuario = (usuario) => {
    const usuarioActualizado = {...usuario, disponible: !usuario.disponible};
    axios.post('http://localhost:8080/usuarios', usuarioActualizado)
      .then(() => {
        console.log("Estado de usuario actualizado");
        cargarUsuarios();
      })
      .catch(error => {
        console.error('Error al actualizar usuario:', error);
        alert('Error al actualizar el estado del usuario');
      });
  };
  
  if (loading) {
    return <div className="loading">Cargando datos...</div>;
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
  
  return (
    <div className="leader-dashboard">
      <h1>Panel de Control - Líder de Equipo</h1>
      
      <div className="control-panel">
        <button className="ejecutar-agente" onClick={ejecutarAgente}>
          Ejecutar Agente de Asignación
        </button>
      </div>
      
      <div className="nueva-tarea">
        <h2>Crear Nueva Tarea</h2>
        <form onSubmit={crearNuevaTarea}>
          <div className="form-group">
            <label>Título</label>
            <input 
              type="text" 
              value={nuevaTarea.descripcion} 
              onChange={(e) => setNuevaTarea({...nuevaTarea, descripcion: e.target.value})} 
              required 
            />
          </div>
          <div className="form-group checkbox">
            <label>
              <input 
                type="checkbox" 
                checked={nuevaTarea.critica} 
                onChange={(e) => setNuevaTarea({...nuevaTarea, critica: e.target.checked})}
              />
              Tarea crítica
            </label>
          </div>
          <button type="submit" className="create-btn">Crear Tarea</button>
        </form>
      </div>
      
      <div className="trabajadores">
        <h2>Trabajadores Disponibles</h2>
        {usuarios.length === 0 ? (
          <p>No hay trabajadores registrados.</p>
        ) : (
          <ul className="usuarios-list">
            {usuarios.map(usuario => (
              <li key={usuario.id} className={`usuario-item ${usuario.disponible ? 'disponible' : 'nodisponible'}`}>
                <div className="usuario-info">
                  <h3>{usuario.nombre}</h3>
                  <p>Nivel: {usuario.nivel}</p>
                  <p>Estado: {usuario.disponible ? 'Disponible' : 'No disponible'}</p>
                  <p>Tareas asignadas: {usuario.tareasAsignadas || 0}</p>
                </div>
                <button 
                  className="toggle-btn"
                  onClick={() => cambiarEstadoUsuario(usuario)}
                  disabled={usuario.tareasAsignadas >= 4}
                >
                  {usuario.disponible ? 'Marcar no disponible' : 'Marcar disponible'}
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
      
      <div className="tareas-pendientes">
        <h2>Tareas Pendientes</h2>
        {tareas.filter(tarea => !tarea.finalizada).length === 0 ? (
          <p>No hay tareas pendientes.</p>
        ) : (
          <ul className="tareas-list">
            {tareas.filter(tarea => !tarea.finalizada).map(tarea => (
              <li key={tarea.id} className={`tarea-item ${tarea.critica ? 'critica' : ''}`}>
                <h3>{tarea.descripcion}</h3>
                <p>Crítica: {tarea.critica ? 'Sí' : 'No'}</p>
                <p>Asignada a: {
                  tarea.asignadoA ? 
                    usuarios.find(u => u.id === tarea.asignadoA)?.nombre || 'Desconocido' : 
                    'Sin asignar'
                }</p>
              </li>
            ))}
          </ul>
        )}
      </div>
      
      <div className="notificaciones">
        <h2>Actividad Reciente</h2>
        {notificaciones.length === 0 ? (
          <p>No hay actividad reciente.</p>
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

export default LeaderDashboard;