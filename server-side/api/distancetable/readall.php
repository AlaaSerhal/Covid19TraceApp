<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once '../config/config.php';
include_once '../objects/distanceTable.php';

$database = new Database();
$db = $database->getConnection();

$distanceTable= new distanceTable($db);

// query products
$stmt = $distanceTable->read();
$num = $stmt->rowCount();
  
// check if more than 0 record found
if($num>0){
    $contact_arr=array();
    $contact_arr["records"]=array();

    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        extract($row);
  
        $contact_item=array(
            "userID1" => $userID1,
            "userID2" => $userID2,
            "distance" => $distance);
  
        array_push($contact_arr["records"], $contact_item);
    }
  
   
    http_response_code(200);
    echo json_encode($contact_arr);
}
else{
  
    http_response_code(404);
  
   
    echo json_encode(
        array("message" => "No contacts found.")
    );
}
  

  
