<?php
function activationHash($input){
    $salt = sha1(rand());
    $salt = substr($salt, 0, 10);
    return sha1($salt . $input . $salt);
}

require_once("functions.php");
setErrorHeader("UNKNOWN_ERROR");
require_once("config.php");

$username = "";
$passwd = "";
$email = "";

//check if the Headerfield action is set and correct
checkAction("REGISTER");

//check if the Data is ok
if(isset($_POST[getHeaderName("USERNAME")]) && isset($_POST[getHeaderName("PASSWORD")]) && isset($_POST[getHeaderName("EMAIL")])){
    $username = $_POST[getHeaderName("USERNAME")];
    $passwd = $_POST[getHeaderName("PASSWORD")];
    $email = $_POST[getHeaderName("EMAIL")];
}else{
    error("EMPTY_FIELD");
}

//need more checks if Username is ok, email valid

//check if Email ist already in use
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
if(sizeof($valid) != 0){
    error("EMAIL_TAKEN");
}
$con = null;

//check if Username aready in use    
try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "SELECT * FROM users WHERE username = :username";
    $stmt = $con->prepare($sql);
    $stmt->bindValue("username", $username);
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
if(sizeof($valid) != 0){
    error("USERNAME_TAKEN");
}
$con = null;

$passwd_hash = password_hash($passwd,PASSWORD_DEFAULT);
$activation_hash = activationHash($email);
try{    
    $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
    //$con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
    $sql = "INSERT INTO users (email, username, password, code, created) VALUES(:email, :username, :password, :code, NOW())";
    $stmt = $con->prepare($sql);
    $stmt->bindValue("username", $username);
    $stmt->bindValue("email", $email);
    $stmt->bindValue("password", $passwd_hash);
    $stmt->bindValue("code", $activation_hash);
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
$con = null;

setErrorHeader("NO_ERROR");
?>