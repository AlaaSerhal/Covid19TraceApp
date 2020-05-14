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
include_once '../objects/distanceTable.php';
//include_once '../distancetable/getcontacts.php';
  
$database = new Database();
$db = $database->getConnection();
  
$user = new User($db);
$distancetable= new distanceTable($db); 
// get posted data
$data = json_decode(file_get_contents("php://input"));
  
// make sure data is not empty
if(
    !empty($data->userID)
){
  
    // set product property values
    $user->userID = $data->userID;
    $user->isILL = $data->isILL;
    // create the product
    if($user->updatestate()){
    $distancetable->userID1 = $data->userID;
    $stmt=$distancetable->getcontacts();
    $num = $stmt->rowCount(); 
    if($num>0){
        $contact_arr=array();
        $contact_arr["records"]=array();
    
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
            extract($row);
      
           // $contact_item=array(
             //   "User" => $userID2,
               // "mobileNumber" => $mobileNumber , 
				//"oneSignalId" => $oneSignalId);
		if($oneSignalId != null){
			sendCovidNotification($oneSignalId);}
            
            
        }}

    
  
        // set response code - 201 created
        http_response_code(201);
  
        // tell the user
        echo json_encode(array("message" => "User is updated"));
    }
  
    // if unable to create the product, tell the user
    else{
  
        // set response code - 503 service unavailable
        http_response_code(503);
  
        // tell the user
        echo json_encode(array("message" => "Can't Update User,user not found"));
    }
}
  
// tell the user data is incomplete
else{
  
    // set response code - 400 bad request
    http_response_code(400);
  
    // tell the user
    echo json_encode(array("message" => "Incomplete Data."));
}

function sendCovidNotification($oneSignalId){
    $content = array('en'	=>  "Please self-isolate or seek medical help since you were in contact with an infected individual");
    
    $fields = array(
      'app_id' => "6a35f1e8-7b59-484f-8c3e-878d42c28f87",
	  'include_player_ids' => array($oneSignalId),
	  'isAndroid' => true,
      'data' => array("ops" => "1" , "send"=> true),
	  'content_available' => true,
      'contents' => $content
    );
    
    $fields = json_encode($fields);
    
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json',
                           'Authorization: Basic MDJkM2VkNjgtZDU0Ni00OTEzLWFiYjctODlhYjM5NWE2YzYy'));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($ch, CURLOPT_HEADER, FALSE);
    curl_setopt($ch, CURLOPT_POST, TRUE);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);

    $response = curl_exec($ch);
    curl_close($ch);

    $result = json_decode($response);
    if($result->recipients>0)
	return true;
    else
	return false;

  }
?>