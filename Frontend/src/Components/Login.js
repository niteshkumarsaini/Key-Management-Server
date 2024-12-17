import React, { useEffect, useState } from "react";
import BackendService from "../Services/Service";
import {
  TextField,
  Button,
  Typography,
  Link as MuiLink,
  Box,
  Paper,
  Alert,
} from "@mui/material";
import { useLocation, useNavigate, Link } from "react-router-dom"; // Import Link from react-router-dom
import axios from 'axios';
import { jwtDecode } from "jwt-decode";


const Login = () => {
  useEffect(() => {
    document.title = "Login";
  }, []);

  const location = useLocation(); // Hook to access the router's state
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState(""); 
  const navigate = useNavigate();
  const [type, setType] = useState("info");

  // If logout message exists in the location state, set it
  useEffect(() => {
    if (location.state && location.state.message) {
      setMessage(location.state.message);
      setType("info");
    }
  }, [location]);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Login Data", { username, password });
    // Calling backend
    axios.post("http://localhost:9000/api/auth/login", { username, password })
      .then((response) => {
        
        console.log(response.data);
        console.log(response.data.token)

        //set the user active
     
      BackendService.saveUser(response.data.token);

        //fetching role from backend of the user
        axios.get("http://localhost:9000/api/auth/getRole",{
          headers: { 
            "Content-Type": "application/json",
            Authorization: `Bearer ${response.data.token}`,
            
          },}).then((response)=>{
          console.log(response.data);
          let user=response.data;
          navigate("/dashboard",{state:{message:user}});

        }).catch((err)=>{
          console.log(err);
          
        })

    
       
      })
      .catch((err) => {
        console.log(err);
        setMessage("Invalid Username or Password!");
        setType("error");
      });
  };

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        backgroundColor: "#F4F5F7",
        padding: "2rem",
      }}
    >
      <Paper
        elevation={12}
        sx={{
          padding: "3rem",
          width: "450px",
          borderRadius: "10px",
          backgroundColor: "white",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.1)",
          textAlign: "center",
        }}
      >
        <Box
          component="img"
          src="/logo.png"
          alt="Company Logo"
          sx={{
            height: "30px",
            width: "auto",
            marginBottom: "1.5rem",
            objectFit: "contain",
          }}
        />
        
        <Typography
          variant="h4"
          sx={{
            fontWeight: "bold",
            color: "#333333",
            marginBottom: "1rem",
            fontFamily: "'Roboto', sans-serif",
          }}
        >
          Login
        </Typography>
        <Typography
          variant="body1"
          sx={{
            color: "#666666",
            fontSize: "0.95rem",
            marginBottom: "2rem",
          }}
        >
          Login to your account and access the dashboard.
        </Typography>

        {/* Display logout message if exists */}
        {message && (
          <Alert severity={type} sx={{ marginBottom: "1rem" }}>
            {message}
          </Alert>
        )}

        <form onSubmit={handleSubmit}>
          <TextField
            label="Username"
            variant="outlined"
            fullWidth
            required
            sx={{
              mb: 3,
              "& .MuiOutlinedInput-root": {
                borderRadius: "8px",
              },
            }}
            placeholder="Enter your username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            required
            sx={{
              mb: 3,
              "& .MuiOutlinedInput-root": {
                borderRadius: "8px",
              },
            }}
            placeholder="Enter your password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 3,
            }}
          >
            <MuiLink
              component={Link} // Use Link from react-router-dom
              to="/register" // Use to instead of href
              underline="hover"
              sx={{
                fontSize: "0.9rem",
                color: "#1976D2",
                fontWeight: "500",
                "&:hover": {
                  textDecoration: "underline",
                },
              }}
            >
              Register New
            </MuiLink>
            <MuiLink
              href="#"
              underline="hover"
              sx={{
                fontSize: "0.9rem",
                color: "#1976D2",
                fontWeight: "500",
                "&:hover": {
                  textDecoration: "underline",
                },
              }}
            >
              Forgot Password?
            </MuiLink>
          </Box>

          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{
              padding: "0.9rem",
              fontSize: "1rem",
              fontWeight: "bold",
              color: "#FFFFFF",
              backgroundColor: "#1976D2",
              borderRadius: "8px",
              transition: "all 0.3s ease",
              "&:hover": {
                backgroundColor: "#1565C0",
              },
            }}
          >
            LOGIN
          </Button>
        </form>
      </Paper>
    </Box>
  );
};

export default Login;
