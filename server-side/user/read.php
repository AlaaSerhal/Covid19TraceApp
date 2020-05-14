<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once '../config/config.php';
include_once '../objects/User.php';

$database = new Database();
$db = $database->getConnection();

if($db == null){
 echo json_encode(
        array("message" => "Can not connect to the database")
    );
}

$User= new User($db);

// query products
$stmt = $User->read();
$num = $stmt->rowCount();

  
// check if more than 0 record found
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
    }
  
   
    http_response_code(200);
    echo json_encode($user_arr);
}
else{
  
    http_response_code(404);
  
   
    echo json_encode(
        array("message" => "No users found.")
    );
}
  

  
