import React, { useEffect, useState } from "react";
import { Button, Box, Typography, TextField, Paper, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle,Alert } from "@mui/material";
import * as XLSX from "xlsx";
import { useNavigate } from "react-router-dom";
import { useLocation } from 'react-router'
import axios from "axios";
import BackendService from "../Services/Service";

const Dashboard = () => {

  const navigate = useNavigate();
  useEffect(() => {
    document.title = "Dashboard";
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

  const [admin1Key, setAdmin1Key] = useState("");
  const [admin2Key, setAdmin2Key] = useState("");
  const [generatedKey, setGeneratedKey] = useState("");
  const [openDialog, setOpenDialog] = useState(false);
  const location = useLocation(); 
  const [active,setActive]=useState("");
  const [errorMessage,setErrorMessage]=useState("Something went wrong..");
  const [isError,setIsError]=useState(false);
  const [type,setType]=useState("info");
  const handleLogout = () => {
    // console.log("User logged out");
    setTimeout(() => {
      BackendService.logoutUser();
      navigate("/login",{state:{ message: "You have been logged out successfully." }}); // Redirect to the login page after a delay
    }); 
  };

  const handleGenerateDEK = () => {
    if (!admin1Key || !admin2Key) {
      alert("Please provide both Admin1 Key and Admin2 Key before generating the DEK.");
      return;
    }
    //Calling Backend
//Generating Admin1 Key
//Getting Token from Storage
let token=BackendService.getToken();
//Calling Backend service to generate key
let adminkeys={
  "admin1EncryptedKey":admin1Key,
  "admin2EncryptedKey":admin2Key
}

axios.post("http://localhost:9000/api/keys/generateDEK",adminkeys,{
  headers: { 
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
    
  },}).then((response)=>{
    let dek=response.data.encryptedDEK;
  // console.log(response.data.encryptedDEK);
 
  setGeneratedKey(dek);
  setOpenDialog(true);
}).catch((err)=>{
  if(err.message==="Request failed with status code 401"){
    //logout user
    BackendService.logoutUser();
    // console.log("You have not permission");
    navigate("/login",{state:{message:"You have been logged out."}})
   }
})
  };

    useEffect(() => {
      // console.log(location.state)
      if (location.state && location.state.message) {
    setActive(location.state.message);
    // console.log(location.state.message);
       
      }
    }, [location]);
  const handleGenerateAdmin1Key = () => {
    const admin1GeneratedKey = "Admin1-Key-67890-FGHIJ"; // Replace with dynamic logic
    // console.log("Admin1 Key Generated:", admin1GeneratedKey);
//Generating Admin1 Key
//Getting Token from Storage
let token=BackendService.getToken();
//Calling Backend service to generate key
setAdmin1Key("Generating..")
axios.get("http://localhost:9000/api/keys/admin1",{
  headers: { 
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
    
  },}).then((response)=>{
  // console.log(response.data);
  setAdmin1Key(response.data.encryptedAdmin1Key);
}).catch((err)=>{
  if(err.message==="Request failed with status code 401"){
    //logout user
    BackendService.logoutUser();
    // console.log("You have not permission");
    navigate("/login",{state:{message:"You have been logged out."}})
   }
   setIsError(true);
   setErrorMessage(err.message)
   setType("error");
    
  
})  
  };

  const handleGenerateAdmin2Key = () => {
    const admin2GeneratedKey = "Admin2-Key-09876-UVWXY"; // Replace with dynamic logic
    // console.log("Admin2 Key Generated:", admin2GeneratedKey);
//Generating Admin2 Key
//Generating Admin1 Key
//Getting Token from Storage
let token=BackendService.getToken();
//Calling Backend service to generate key
setAdmin2Key("Generating..")
axios.get("http://localhost:9000/api/keys/admin2",{
  headers: { 
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
    
  },}).then((response)=>{
  // console.log(response.data);
  setAdmin2Key(response.data.encryptedAdmin2Key);
}).catch((err)=>{
 if(err.message==="Request failed with status code 401"){
  //logout user
  BackendService.logoutUser();
  // console.log("You have not permission");
  navigate("/login",{state:{message:"You have been logged out."}})
 }
  
})

  };

  const handleSaveToExcel = () => {
    const data = [{ EncryptedDEK: generatedKey }];
    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "DEK");
    XLSX.writeFile(workbook, "Generated_DEK.xlsx");
    setOpenDialog(false);
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
          Admin Dashboard
        </Typography>
            {/* Display logout message if exists */}
                {isError && (
                  <Alert severity={type} sx={{ marginBottom: "1rem" }}>
                    {errorMessage}
                  </Alert>
                )}
        <Box sx={{ marginBottom: "2rem" }}>
          
          {
        active==='ADMIN1'?<Button
        variant="outlined"
        color="secondary"
        fullWidth
        sx={{
          padding: "0.8rem",
          fontSize: "1rem",
          marginBottom: "1rem",
          borderRadius: "8px",
        }}
        onClick={handleGenerateAdmin1Key}
      >
        Generate Admin1 Key
      </Button>:<Button
            variant="outlined"
            color="secondary"
            fullWidth
            sx={{
              padding: "0.8rem",
              fontSize: "1rem",
              marginBottom: "1rem",
              borderRadius: "8px",
            }}
            onClick={handleGenerateAdmin2Key}
          >
            Generate Admin2 Key
          </Button>
          }
          
          
        </Box>

        <Box sx={{ marginBottom: "2rem" }}>
          <TextField
            label="Admin1 Key"
            variant="outlined"
            fullWidth
            value={admin1Key}
            onChange={(e) => setAdmin1Key(e.target.value)}
            sx={{
              marginBottom: "1rem",
              "& .MuiOutlinedInput-root": { borderRadius: "8px" },
            }}
          />
          <TextField
            label="Admin2 Key"
            variant="outlined"
            fullWidth
            value={admin2Key}
            onChange={(e) => setAdmin2Key(e.target.value)}
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
          onClick={handleGenerateDEK}
        >
          Generate DEK
        </Button>

        {generatedKey && (
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
              Generated Key:
            </Typography>
            <Typography
              variant="body1"
              sx={{ wordBreak: "break-word", fontSize: "1rem", color: "#555555" }}
            >
              {generatedKey}
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

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Save DEK</DialogTitle>
        <DialogContent>
          <DialogContentText>Do you want to save the generated DEK?</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="primary">
            No
          </Button>
          <Button onClick={handleSaveToExcel} color="primary" autoFocus>
            Yes
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Dashboard;
