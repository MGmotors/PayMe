<?php
require_once("helper/functions.php");
setErrorHeader("UNKNOWN_ERROR");

$email = "";
$passwd = "";

//check if the Headerfield action is set and correct
checkAction("LOGIN");

$email = getFieldOrDie("EMAIL");
$passwd = getFieldOrDie("PASSWORD");
$passwd_hash = password_hash($passwd, PASSWORD_DEFAULT);

try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "SELECT * FROM users WHERE email = :email";
    $stmt = $con->prepare($sql);
    $stmt->bindValue( "email", $email);
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
    error("NAME_PW_MISMATCH");
}



if(!password_verify($passwd,$valid[0]["password"])){
    error("NAME_PW_MISMATCH");
}

$con = null;

session_start();
$_SESSION["uid"] = $valid[0]["id"];

setErrorHeader("NO_ERROR");
?>