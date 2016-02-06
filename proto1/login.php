<?php
$required_action = "login_user";
$username = "";
$passwd = "";
require_once("functions.php");
require_once("MyDB.php");

//check if the Headerfield action is set and correct
checkAction($required_action);

//check if the Data is ok
if(isset($_POST["payme-username"]) && isset($_POST["payme-passwd"])){
    $username = $_POST["payme-username"];
    $passwd = $_POST["payme-passwd"];
}else{
    error("data", "The provided data was not sufficent");
}

$mydb = new MyDB();
$sql = "SELECT * FROM users2 WHERE name = ? and encrypted_password = ?";
$a = array();
$a[] = $username;
$a[] = $passwd;
$result = $mydb->query($sql,"ss",$a);
if(!$result){
    error("Checking Error", $mydb->getError());
}
if(sizeof($mydb->getRows()) != 1){
    error("login failed", "Username or passwort doesn't match");
}

header('payme-status: ok');
print("succsess");
?>