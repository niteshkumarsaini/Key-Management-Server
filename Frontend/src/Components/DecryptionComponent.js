import React, { useEffect, useState } from "react";
import { Button, Box, Typography, TextField, Paper, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import BackendService from "../Services/Service";

const DecryptionComponent = () => {
  const navigate = useNavigate();
  const [encryptedDEK, setEncryptedDEK] = useState("");
  const [decryptData, setDecryptData] = useState("");
  const [encryptedSensitiveData, setEncryptedSensitiveData] = useState("");
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  useEffect(() => {
    document.title = "Decrypt Data";
    if (!BackendService.isUser()) {
      navigate("/login");
    } else {
      const token = localStorage.getItem("token");

      if (!token) {
        navigate("/login");
      } else {
        axios.get("http://localhost:9000/api/auth/checkTokenExpired", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then(response => {
          console.log("Token is valid:", response.data);
        })
        .catch(error => {
            
          console.error("Token is expired or invalid:", error);
          BackendService.logoutUser();
          navigate("/login");
        });
      }
    }
  }, [navigate]);

  const handleLogout = () => {
    // console.log("User logged out");
    setTimeout(() => {
      BackendService.logoutUser();
      navigate("/login", { state: { message: "You have been logged out successfully." } });
    });
  };

  const decryptSensitiveData = () => {
    if (!encryptedDEK || !encryptedSensitiveData) {
      alert("Please provide both Encrypted DEK Key and Sensitive Data before Decryption.");
      return;
    }

    const token = BackendService.getToken();
    const decryptionObj = {
      encryptedData: encryptedSensitiveData,
      encryptedDEK: encryptedDEK,
    };

    // Send request to backend to decrypt data
    axios.post("http://localhost:9000/api/data/decrypt", decryptionObj, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
    .then((response) => {
    //   console.log(response.data);
      setDecryptData(response.data.decryptedData);
      setMessage("Data decrypted successfully.");
      setType("success");
    })
    .catch((err) => {
      if (err.message === "Request failed with status code 401") {
        BackendService.logoutUser();
        // console.log("You have no permission");
        navigate("/login", { state: { message: "You have been logged out." } });
      } else {
        setMessage("Error decrypting data. Please try again.");
        setType("error");
      }
    });
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        backgroundColor: "#F9FAFC",
        padding: "2rem",
      }}
    >
      <Paper
        elevation={12}
        sx={{
          padding: "2rem",
          width: "600px",
          borderRadius: "10px",
          backgroundColor: "white",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.1)",
          textAlign: "center",
        }}
      >
        <Typography variant="h4" sx={{ fontWeight: "bold", color: "#333333", marginBottom: "2rem" }}>
          Decryption Dashboard
        </Typography>

        {/* Display logout message if exists */}
        {message && (
          <Alert severity={type} sx={{ marginBottom: "1rem" }}>
            {message}
          </Alert>
        )}

        <Box sx={{ marginBottom: "2rem" }}>
          <TextField
            label="Encrypted DEK"
            variant="outlined"
            fullWidth
            value={encryptedDEK}
            onChange={(e) => setEncryptedDEK(e.target.value)}
            sx={{
              marginBottom: "1rem",
              "& .MuiOutlinedInput-root": { borderRadius: "8px" },
            }}
          />
          <TextField
            label="Encrypted Sensitive Data"
            variant="outlined"
            fullWidth
            value={encryptedSensitiveData}
            onChange={(e) => setEncryptedSensitiveData(e.target.value)}
            sx={{ "& .MuiOutlinedInput-root": { borderRadius: "8px" } }}
          />
        </Box>

        <Button
          variant="contained"
          color="primary"
          fullWidth
          sx={{
            padding: "0.8rem",
            fontSize: "1rem",
            marginBottom: "2rem",
            borderRadius: "8px",
          }}
          onClick={decryptSensitiveData}
        >
          Decrypt Sensitive Data
        </Button>

        {decryptData && (
          <Box
            sx={{
              border: "1px solid #E0E0E0",
              borderRadius: "8px",
              padding: "1rem",
              backgroundColor: "#F9FAFC",
              marginBottom: "2rem",
            }}
          >
            <Typography variant="h6" sx={{ fontWeight: "500", color: "#333333", marginBottom: "0.5rem" }}>
              Decrypted Sensitive Data:
            </Typography>
            <Typography
              variant="body1"
              sx={{ wordBreak: "break-word", fontSize: "1rem", color: "#555555" }}
            >
              {decryptData}
            </Typography>
          </Box>
        )}

        <Button
          variant="contained"
          color="error"
          fullWidth
          sx={{
            padding: "0.8rem",
            fontSize: "1rem",
            borderRadius: "8px",
          }}
          onClick={handleLogout}
        >
          Logout
        </Button>
      </Paper>
    </Box>
  );
};

export default DecryptionComponent;
