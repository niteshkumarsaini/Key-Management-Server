import {React,useEffect} from "react";
import { useNavigate } from "react-router-dom";

const ErrorPage = () => {
  const navigate = useNavigate();
  
  useEffect(() => {
    document.title = "Page Not Found";
  });

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <img
          src="https://cdn-icons-png.flaticon.com/512/753/753345.png" 
          alt="Error Icon"
          style={styles.image}
        />
        <h1 style={styles.errorCode}>404</h1>
        <h2 style={styles.message}>Oops! Page Not Found</h2>
        <p style={styles.description}>
          The page you're looking for doesn't exist or has been moved. Please check the URL or return to the homepage.
        </p>
        <button style={styles.button} onClick={() => navigate("/login")}>
          Go to Login
        </button>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    height: "100vh",
    background: "linear-gradient(135deg, #f1f4f9, #dff1ff)",
    fontFamily: "Arial, sans-serif",
    padding: "0 20px",
  },
  card: {
    textAlign: "center",
    background: "#ffffff",
    padding: "30px",
    borderRadius: "12px",
    boxShadow: "0px 10px 30px rgba(0, 0, 0, 0.1)",
    maxWidth: "500px",
    width: "100%",
  },
  image: {
    width: "100px",
    height: "100px",
    marginBottom: "20px",
  },
  errorCode: {
    fontSize: "72px",
    fontWeight: "700",
    color: "#ff4a4a",
    margin: "0",
  },
  message: {
    fontSize: "24px",
    fontWeight: "600",
    color: "#333",
    margin: "10px 0",
  },
  description: {
    fontSize: "16px",
    color: "#555",
    marginBottom: "20px",
    lineHeight: "1.6",
  },
  button: {
    padding: "12px 24px",
    backgroundColor: "#007bff",
    color: "#fff",
    fontSize: "16px",
    fontWeight: "bold",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    textTransform: "uppercase",
  },
  buttonHover: {
    backgroundColor: "#0056b3",
  },
};

export default ErrorPage;
