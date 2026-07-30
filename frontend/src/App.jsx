import { useState, useEffect } from 'react'
import axios from 'axios'

function App() {
  const [message, setMessage] = useState('Loading...');

  useEffect(() => {
    axios.get('http://localhost:8080/api/hello')
      .then((response) => {
        setMessage(response.data);
      })
      .catch((error) => {
        console.error('Error fetching data:', error);
        setMessage('Error — check console');
      });
  }, []);

  return (
    <div>
      <h1>{message}</h1>
    </div>
  );
}

export default App
