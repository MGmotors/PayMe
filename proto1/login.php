<?php


require_once("functions.php");
setErrorHeader("UNKNOWN_ERROR");

$email = "";
$passwd = "";
require_once("config.php");

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
        file_put_contents( 'logs/dbErrors.txt', $arr[2] . "\n", FILE_APPEND );
        error("DATABASE_ERROR");
    }
    $valid = $stmt->fetchAll();
}catch(PDOException $e) {
    file_put_contents( 'logs/dbErrors.txt', $e->getMessage() . "\n", FILE_APPEND );
    error("DATABASE_ERROR");
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