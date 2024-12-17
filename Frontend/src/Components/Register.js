import React, { useEffect, useState } from "react";
import {
  TextField,
  Button,
  Typography,
  Box,
  Paper,
  MenuItem,
  Select,
  FormControl,
  InputLabel,Alert
} from "@mui/material";


import { Link,useNavigate } from "react-router-dom";
import axios from 'axios';

const Register = () => {

    useEffect(()=>{
      document.title="Register"
    })

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");
  const [message, setMessage] = useState(""); 
  const navigate = useNavigate();
  const [type, setType] = useState("info");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Registration Data", { username, password, role });
   // Calling backend
   axios.post("http://localhost:9000/api/auth/signup", { username, password,role })
   .then((response) => {
     console.log(response.data);
     navigate("/login",{state:{message:"Admin has been created Successfully."}});
   })
   .catch((err) => {
     console.log(err);
     console.log(err.response.data)
     setMessage(err.response.data);

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
        backgroundColor: "#F4F5F7", // Neutral corporate background
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
        {/* Logo Section */}
        <Box
          component="img"
          src="/logo.png" // Replace with your logo path
          alt="Company Logo"
          sx={{
            height: "30px",
            width: "auto",
            marginBottom: "1.5rem",
            objectFit: "contain",
          }}
        />

        {/* Header Section */}
        <Typography
          variant="h4"
          sx={{
            fontWeight: "bold",
            color: "#333333",
            marginBottom: "1rem",
            fontFamily: "'Roboto', sans-serif",
          }}
        >
          Create Your Account
        </Typography>
        <Typography
          variant="body1"
          sx={{
            color: "#666666",
            fontSize: "0.95rem",
            marginBottom: "2rem",
          }}
        >
          Please fill in your details below to register your account.
        </Typography>
        
        {message && (
                  <Alert severity={type} sx={{ marginBottom: "1rem" }}>
                    {message}
                  </Alert>
                )}

        {/* Form Section */}
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

          <FormControl
            fullWidth
            sx={{
              mb: 3,
            }}
          >
            <InputLabel id="role-select-label">Role</InputLabel>
            <Select
              labelId="role-select-label"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              label="Role"
              sx={{
                borderRadius: "8px",
              }}
            >
              <MenuItem value="ADMIN1">Admin1</MenuItem>
              <MenuItem value="ADMIN2">Admin2</MenuItem>
              {/* <MenuItem value="Manager">Manager</MenuItem> */}
            </Select>
          </FormControl>

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
            Register
          </Button>
        </form>

        {/* Footer Section */}
        <Typography
          variant="body2"
          sx={{
            marginTop: "1.5rem",
            color: "#999999",
          }}
        >
          Already have an account?{" "}
          <Link 
            to="/login"
            style={{
              textDecoration: "none",
              color: "#1976D2",
              fontWeight: "bold",
            }}
          >
            Login here
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
};

export default Register;
