import './App.css';
import Dashboard from './Components/Dashboard';
import DecryptionComponent from './Components/DecryptionComponent';
import EncryptDecryptComponent from './Components/EncryptionComponent';
import ErrorPage from './Components/ErrorPage';
import Login from './Components/Login';
import Register from './Components/Register';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Redirect '/' to '/login' */}
        <Route path="/" element={<Navigate to="/login" replace />} />
        
        {/* Define routes for other pages */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/encrypt" element={<EncryptDecryptComponent />} />
        <Route path="/decrypt" element={<DecryptionComponent />} />
        
        {/* Catch-all route for undefined paths */}
        <Route path="*" element={<Navigate to="/error" replace />} />
        
        {/* Define a custom error page route */}
        <Route path="/error" element={<ErrorPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
