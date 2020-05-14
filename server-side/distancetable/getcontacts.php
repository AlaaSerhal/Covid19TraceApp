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
    !empty($data->userID1)
   
){ 
    $contact->userID1 = $data->userID1;
    $stmt = $contact->getcontacts();
    $num = $stmt->rowCount(); 
    if($num>0){
        $contact_arr=array();
        $contact_arr["records"]=array();
    
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
            extract($row);
      
            $contact_item=array(
                "User" => $userID2,
                "mobileNumber" => $mobileNumber);
      
            array_push($contact_arr["records"], $contact_item);
            
        }
        echo json_encode($contact_arr);}
        else{
            echo json_encode(array("message" => "No contact with this ID"));
        }
        
}
else {
    echo json_encode(array("message" => "No input"));
}