<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
// get database connection
include_once '../config/config.php';
  
// instantiate product object
include_once '../objects/User.php';
  
$database = new Database();
$db = $database->getConnection();
  
$user = new User($db);


// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// make sure data is not empty
if(
    !empty($data->userID) 
   
){ 
    $user->userID = $data->userID;
    $stmt = $user->readfromid();
    $num = $stmt->rowCount(); 
    if($num>0){
        $user_arr=array();
        $user_arr["records"]=array();
    
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
            extract($row);
      
            $user_item=array(
                "userID" => $userID,
                "fullName" => $fullName,
                "mobileNumber" => $mobileNumber,
                "isILL" => $isILL);
      
            array_push($user_arr["records"], $user_item);
            echo json_encode($user_arr);
        }}
        else{
            echo json_encode(array("message" => "No user with this ID"));
        }
        
}
else {
    echo json_encode(array("message" => "No input from user"));
}