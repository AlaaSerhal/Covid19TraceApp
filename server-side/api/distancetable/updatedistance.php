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
include_once '../objects/distanceTable.php';
  
$database = new Database();
$db = $database->getConnection();
  
$contact = new distanceTable($db);
  
// get posted data
$data = json_decode(file_get_contents("php://input"));
  
// make sure data is not empty
if(
    !empty($data->userID1) && !empty($data->userID2)
){
  
    // set product property values
    $contact->userID1 = $data->userID1;
    $contact->userID2 = $data->userID2;
    $contact->distance = $data->distance;
    // create the product
    if($contact->updatedistance()){
  
        // set response code - 201 created
        http_response_code(201);
  
        // tell the contact
        echo json_encode(array("message" => "Distance is updated"));
    }
  
    // if unable to create the product, tell the contact
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the contact
        echo json_encode(array("message" => "Can't Update distance,contact not found"));
    }
}
  
// tell the contact data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the contact
    echo json_encode(array("message" => "Incomplete Data."));
}
?>