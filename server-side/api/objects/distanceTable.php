<?php
class distanceTable{
    private $conn;
    private $table_name = "distancetable";
  
    public $userID1;
    public $userID2;
    public $distance;

    public function __construct($db){
        $this->conn = $db;
    }
    function read(){
  
        // select all query
        $query = "SELECT
                    u.userID1, u.userID2, u.distance
                FROM
                    " . $this->table_name . " u";
      
        // prepare query statement
        $stmt = $this->conn->prepare($query);
      
        // execute query
        $stmt->execute();
      
        return $stmt;
    }
        function create(){
  
            // query to insert record
                $query = "INSERT INTO
                " . $this->table_name . "
            SET
                userID1=:userID1, userID2=:userID2, distance=:distance";
  
    // prepare query
    $stmt = $this->conn->prepare($query);
  
    // sanitize
    $this->userID1=htmlspecialchars(strip_tags($this->userID1));
    $this->userID2=htmlspecialchars(strip_tags($this->userID2));
    $this->distance=htmlspecialchars(strip_tags($this->distance));
    
    
  
    // bind values
    $stmt->bindParam(":userID1", $this->userID1);
    $stmt->bindParam(":userID2", $this->userID2);
    $stmt->bindParam(":distance", $this->distance);
    
            
            // execute query
            if($stmt->execute()){
                return true;
            }
          
            return false;
            
        }
        function isexist(){
            

        $query= "SELECT * FROM distancetable WHERE userID1=:userID1 AND userID2=:userID2";
        $stmt = $this->conn->prepare($query);
        $this->userID1=htmlspecialchars(strip_tags($this->userID1));
        $stmt->bindParam(":userID1", $this->userID1);
        $this->userID2=htmlspecialchars(strip_tags($this->userID2));
        $stmt->bindParam(":userID2", $this->userID2);
        
        $stmt->execute();
        return $stmt;
            }


    function getdistance(){

        $query= "SELECT distance FROM distancetable u WHERE userID1=:userID1 AND userID2=:userID2 ";
        $stmt = $this->conn->prepare($query);
        $this->userID1=htmlspecialchars(strip_tags($this->userID1));
        $stmt->bindParam(":userID1", $this->userID1);
        $this->userID2=htmlspecialchars(strip_tags($this->userID2));
        $stmt->bindParam(":userID2", $this->userID2);
        $stmt->execute();
      
        return $stmt;
    
    }

    function updatedistance(){
  
        // query to insert record
        $query = "UPDATE " . $this->table_name . " SET distance=:distance WHERE userID1=:userID1 AND userID2=:userID2";
      
        // prepare query
        $stmt = $this->conn->prepare($query);
      
        // sanitize
        $this->userID1=htmlspecialchars(strip_tags($this->userID1));
        $this->userID2=htmlspecialchars(strip_tags($this->userID2));
        $this->distance=htmlspecialchars(strip_tags($this->distance));
        // bind values
        $stmt->bindParam(":userID1", $this->userID1);
        $stmt->bindParam(":userID2", $this->userID2);
        $stmt->bindParam(":distance", $this->distance);
       
      
        // execute query
        if($stmt->execute()){
            return true;
        }
      
        return false;
          
    }
}

?>