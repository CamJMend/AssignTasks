import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import WorkerDashboard from './components/WorkerDashboard';
import LeaderDashboard from './components/LeaderDashboard';
import Login from './components/Login';
import { connectWebSocket, disconnectWebSocket } from './services/websocketService';
import './App.css';

function App() {
  const [user, setUser] = useState(null);
  
  useEffect(() => {
    // Conectar WebSocket al iniciar la aplicación
    connectWebSocket()
      .then(() => {
        console.log("WebSocket conectado exitosamente");
      })
      .catch(err => {
        console.error("Error al conectar WebSocket:", err);
        // Puedes añadir un estado para mostrar un mensaje de error en la UI
      });
    
    // Recuperar sesión si existe
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (err) {
        console.error("Error al cargar usuario guardado:", err);
        localStorage.removeItem('user');
      }
    }
    
    return () => {
      // Desconectar WebSocket al cerrar la aplicación
      disconnectWebSocket();
    };
  }, []);
  
  const handleLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };
  
  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };
  
  return (
    <Router>
      <div className="app">
        <header>
          <h1>Sistema de Asignación de Tareas</h1>
          {user && (
            <button className="logout-btn" onClick={handleLogout}>
              Cerrar Sesión
            </button>
          )}
        </header>
        
        <main>
          <Routes>
            <Route 
              path="/" 
              element={user ? <Navigate to={user.isLeader ? "/leader" : `/worker/${user.id}`} /> : <Login onLogin={handleLogin} />} 
            />
            <Route 
              path="/worker/:id" 
              element={user && !user.isLeader ? <WorkerDashboard userId={user.id} /> : <Navigate to="/" />} 
            />
            <Route 
              path="/leader" 
              element={user && user.isLeader ? <LeaderDashboard /> : <Navigate to="/" />} 
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;