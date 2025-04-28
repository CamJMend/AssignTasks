import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Login = ({ onLogin }) => {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedUser, setSelectedUser] = useState('');
  const [isLeader, setIsLeader] = useState(false);
  
  useEffect(() => {
    axios.get('http://localhost:8080/usuarios')
      .then(response => {
        console.log("Usuarios cargados:", response.data);
        setUsuarios(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error al cargar usuarios:', error);
        setError("No se pudieron cargar los usuarios. Verifica que el backend esté funcionando.");
        setLoading(false);
      });
  }, []);
  
  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (isLeader) {
      onLogin({ id: 'leader', nombre: 'Líder de Equipo', isLeader: true });
    } else if (selectedUser) {
      const usuario = usuarios.find(u => u.id === selectedUser);
      if (usuario) {
        onLogin({ ...usuario, isLeader: false });
      }
    }
  };
  
  if (loading) {
    return <div className="loading">Cargando usuarios...</div>;
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
    <div className="login-container">
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>
            <input
              type="checkbox"
              checked={isLeader}
              onChange={() => setIsLeader(!isLeader)}
            />
            Iniciar como Líder de Equipo
          </label>
        </div>
        
        {!isLeader && (
          <div className="form-group">
            <label>Selecciona tu usuario:</label>
            <select 
              value={selectedUser} 
              onChange={(e) => setSelectedUser(e.target.value)}
              required={!isLeader}
              disabled={isLeader}
            >
              <option value="">Seleccionar...</option>
              {usuarios.map(usuario => (
                <option key={usuario.id} value={usuario.id}>
                  {usuario.nombre} ({usuario.nivel})
                </option>
              ))}
            </select>
          </div>
        )}
        
        <button type="submit" className="login-btn">
          Iniciar Sesión
        </button>
      </form>
    </div>
  );
};

export default Login;