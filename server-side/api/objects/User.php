<?php
class User{

    private $conn;
    private $table_name = "User";
  
    // object properties
    public $userID;
    public $fullName;
    public $mobileNumber;
    public $isILL;
    
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read products
function read(){
  
    // select all query
    $query = "SELECT
                u.userID, u.fullName, u.mobileNumber, u.isILL
            FROM
                " . $this->table_name . " u
                ";
  
    // prepare query statement
    $stmt = $this->conn->prepare($query);
  
    // execute query
    $stmt->execute();
  
    return $stmt;
}
function readfromid(){

    $query= "SELECT
    u.userID, u.fullName, u.mobileNumber, u.isILL FROM " . $this->table_name . " u WHERE u.userID=:userID";
    $stmt = $this->conn->prepare($query);
    $this->userID=htmlspecialchars(strip_tags($this->userID));
    $stmt->bindParam(":userID", $this->userID);
    $stmt->execute();
  
    return $stmt;

}
function create(){
  
    // query to insert record
    $query = "INSERT INTO
                " . $this->table_name . "
            SET
                userID=:userID, fullName=:fullName, mobileNumber=:mobileNumber, isILL=:isILL";
  
    // prepare query
    $stmt = $this->conn->prepare($query);
  
    // sanitize
    $this->userID=htmlspecialchars(strip_tags($this->userID));
    $this->fullName=htmlspecialchars(strip_tags($this->fullName));
    $this->mobileNumber=htmlspecialchars(strip_tags($this->mobileNumber));
    $this->isILL=htmlspecialchars(strip_tags($this->isILL));
    
  
    // bind values
    $stmt->bindParam(":userID", $this->userID);
    $stmt->bindParam(":fullName", $this->fullName);
    $stmt->bindParam(":mobileNumber", $this->mobileNumber);
    $stmt->bindParam(":isILL", $this->isILL);
  
    // execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
      
}
function updatestate(){
  
    // query to insert record
    $query = "UPDATE " . $this->table_name . " SET isILL=:isILL WHERE userID=:userID";
  
    // prepare query
    $stmt = $this->conn->prepare($query);
  
    // sanitize
    $this->userID=htmlspecialchars(strip_tags($this->userID));
    $this->isILL=htmlspecialchars(strip_tags($this->isILL));
    // bind values
    $stmt->bindParam(":userID", $this->userID);
    $stmt->bindParam(":isILL", $this->isILL);
  
    // execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
      
}
}
?>