import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

let stompClient = null;
const subscribers = {
  tareas: [],
  usuarios: []
};

export const connectWebSocket = (baseUrl = 'http://localhost:8080') => {
  return new Promise((resolve, reject) => {
    try {
      console.log('Intentando conectar a WebSocket en:', `${baseUrl}/ws`);
      const socket = new SockJS(`${baseUrl}/ws`);
      
      stompClient = Stomp.over(socket);
      stompClient.debug = null;
      
      stompClient.connect({}, 
        frame => {
          console.log('WebSocket conectado!', frame);
          
          stompClient.subscribe('/topic/tareas', message => {
            console.log('Recibido mensaje en /topic/tareas:', message.body);
            subscribers.tareas.forEach(callback => callback(message.body));
          });
          
          stompClient.subscribe('/topic/usuarios', message => {
            console.log('Recibido mensaje en /topic/usuarios:', message.body);
            subscribers.usuarios.forEach(callback => callback(message.body));
          });
          
          resolve(true);
        }, 
        error => {
          console.error('Error de conexión WebSocket:', error);
          reject(error);
        }
      );
      
      socket.onerror = (error) => {
        console.error('Error en el socket:', error);
        reject(error);
      };
      
    } catch (err) {
      console.error('Error al inicializar WebSocket:', err);
      reject(err);
    }
  });
};

export const disconnectWebSocket = () => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.disconnect();
    console.log('WebSocket desconectado');
    return true;
  }
  return false;
};

export const subscribeTareas = (callback) => {
  if (typeof callback !== 'function') {
    console.error('El callback debe ser una función');
    return () => {};
  }
  
  subscribers.tareas.push(callback);
  return () => {
    const index = subscribers.tareas.indexOf(callback);
    if (index !== -1) {
      subscribers.tareas.splice(index, 1);
    }
  };
};

export const subscribeUsuarios = (callback) => {
  if (typeof callback !== 'function') {
    console.error('El callback debe ser una función');
    return () => {};
  }
  
  subscribers.usuarios.push(callback);
  return () => {
    const index = subscribers.usuarios.indexOf(callback);
    if (index !== -1) {
      subscribers.usuarios.splice(index, 1);
    }
  };
};

export const isConnected = () => {
  return stompClient !== null && stompClient.connected;
};