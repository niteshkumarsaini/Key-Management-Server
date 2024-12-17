class BackendService{

static saveUser(token){
localStorage.setItem("token",token)
console.log("token saved successfully",token)
return true;


}

static isUser(){
  let token=localStorage.getItem("token");
  if(token===null || token ===undefined || token===''){

    return false;


  }
  return true;

}

static logoutUser(){
    localStorage.removeItem("token");
    return true;

}
static getToken(){
    
    return localStorage.getItem("token");

}



}
export default BackendService;