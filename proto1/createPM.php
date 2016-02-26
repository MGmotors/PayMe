<?php
    require_once("helper/functions.php");
    session_start();
    ensureLogin();
    
    checkAction("CREATEPM");
    
    $json = json_decode(getFieldOrDie("CREATEPM_JSON"),true);
    if(!$json){
        error("BAD_DATA","could not parse json");
    }    
    
    
    //parse
    $name = $json["name"];
    $description = $json["description"];
    $price = str_replace( ',', '.', $json["price"]);
    $debtors = array_unique($json["debtors"]);
    if(!$name || !$price || !$description || !$debtors || sizeof($debtors) < 1){
        error("BAD_DATA", "name,price,description or debtors empty");
    }

//INSERT new payme
try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "INSERT INTO payme (name,description,user_id,datetime,price) VALUES (:name,:description,:user_id,NOW(),:price)";
    $stmt = $con->prepare($sql);
    $stmt->bindValue( "name", $name);
    $stmt->bindValue( "description", $description);
    $stmt->bindValue( "user_id", $_SESSION["uid"]);
    $stmt->bindValue( "price", $price);
    $succ = $stmt->execute();
    if(!$succ){
        $arr = $stmt->errorInfo();
        dbError($arr[2]);
    }
    $valid = $stmt->fetchAll();
    $paymeid = $con->lastInsertId();
}catch(PDOException $e) {
    dbError($e->getMessage());
}



//GET debtor ID's
try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "SELECT id FROM users WHERE username IN (";
    
    for($i = 0; $i < sizeof($debtors); $i++){
        $sql .= ":user" . $i ; 
        if(($i +1) < sizeof($debtors)){
            $sql .= " , ";
        }
    }
    $sql .= ")";
    $stmt = $con->prepare($sql);
    
    for($i = 0; $i < sizeof($debtors); $i++){
        $stmt->bindValue( ("user" . $i) , $debtors[$i]);
    }
    $succ = $stmt->execute();
    if(!$succ){
        $arr = $stmt->errorInfo();
        dbError($arr[2]);
    }
    $valid = $stmt->fetchAll();
}catch(PDOException $e) {
     dbError($e->getMessage());
}
if(sizeof($valid) < 1){
    error("BAD_DATA","found DebtorID count < 1");
}
$debtorids = $valid;

//Insert debtor <-> payme
try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "INSERT INTO debtor (user_id, payme_id) VALUES ";
    
    
    for($i = 0; $i < sizeof($debtorids); $i++){
        $sql .= "(" .   $debtorids[$i][0] . "," . $paymeid . ")";
         if(($i +1) < sizeof($debtorids)){
            $sql .= " , ";
        }
    }
    $stmt = $con->prepare($sql);
    $succ = $stmt->execute();
    if(!$succ){
        $arr = $stmt->errorInfo();
        dbError($arr[2]);
    }
    $valid = $stmt->fetchAll();
    $id =  $con->lastInsertId();
}catch(PDOException $e) {
    dbError($e->getMessage());
}

if(!$id){
    error("UNKNOWN_ERROR");
}


setErrorHeader("NO_ERROR");
?>