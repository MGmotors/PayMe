<?php

require_once("functions.php");
setErrorHeader("UNKNOWN_ERROR");

$username = "";
$passwd = "";
$email = "";
require_once("MyDB.php");

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

//check username
$mydb = new MyDB();
$sql = "SELECT * FROM users2 WHERE name = ? ";
$a = array();
$a[] = $username;

$result = $mydb->query($sql,"s",$a);
if(!$result){
    error("DATABASE_ERROR");
}
if(sizeof($mydb->getRows())>0){
    error("USERNAME_TAKEN");
}

//check Email
$mydb = new MyDB();
$sql = "SELECT * FROM users2 WHERE email = ?";
$a = array();
$a[] = $email;
$result = $mydb->query($sql,"s",$a);
if(!$result){
    error("DATABASE_ERROR");
}
if(sizeof($mydb->getRows())>0){
    error("EMAIL_TAKEN");
}

$mydb = new MyDB();
$sql = "INSERT INTO users2 (name, email, encrypted_password,registration_code,created)
        VALUES(?,?,?,?,NOW())";
        
$a = array();
$a[] = $username;
$a[] = $email;
$a[] = $passwd;
$a[] = sha1($username . $email);

$result = $mydb->query($sql,"ssss",$a);

if(!$result){
    error("DATABASE_ERROR");
}

setErrorHeader("NO_ERROR");
?>