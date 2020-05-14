<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
// get database connection
include_once '../config/config.php';
  
// instantiate contact object
include_once '../objects/distanceTable.php';
  
$database = new Database();
$db = $database->getConnection(); 
$contact = new distanceTable($db);
$data = json_decode(file_get_contents("php://input"));


if(
    !empty($data->userID1) &&
    !empty($data->userID2) &&
    !empty($data->distance) 
){
   
    // set contact property values
    $contact->userID1 = $data->userID1;
    $contact->userID2 = $data->userID2;
    $contact->distance = $data->distance;
    // create the product
    $stmt = $contact->isexist();
    $num = $stmt->rowCount();
    if($num==0){
    if($contact->create()){
  
        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("message" => "Contact was created."));
    }
  
    // if unable to create the product, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        
        echo json_encode(array("message" => "Unable to create user.User isn't registered"));
    
    }}
    else{
        $contact->updatedistance();
        echo json_encode(array("message" => "Contact already exists, distance Updated"));
        
    }
    
}

  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("message" => "Data is incomplete."));
}
?>