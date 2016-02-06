<?php

$required_action = "create_user";
$username = "";
$passwd = "";
$email = "";
require_once("functions.php");
require_once("MyDB.php");

//check if the Headerfield action is set and correct
if(isset($_POST["payme-action"])){
    if($_POST["payme-action"] != $required_action){
        error("action", "The actions do not match. Required: " . $required_action . " sent: " . $_POST["payme-action"] );
    }
}else{
    error("action", "The action was not specified");
}

//check if the Data is ok
if(isset($_POST["payme-username"]) && isset($_POST["payme-passwd"]) && isset($_POST["payme-email"])){
    $username = $_POST["payme-username"];
    $passwd = $_POST["payme-passwd"];
    $email = $_POST["payme-email"];
}else{
    error("data", "The provided data was not sufficent");
}

//need more checks if Username is ok, email valid

//check if username or passwd already taken
$mydb = new MyDB();
$sql = "SELECT * FROM users2 WHERE name = ? or email = ?";
$a = array();
$a[] = $username;
$a[] = $email;
$result = $mydb->query($sql,"ss",$a);
if(!$result){
    error("Checking Error", $mydb->getError());
}
if(sizeof($mydb->getRows())>0){
    error("Username or email taken", "The username or the Email is already taken");
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
    error("Injection Error", $mydb->getError());
}

header('payme-status: ok');
print("succsess");


?>