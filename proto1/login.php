<?php


require_once("functions.php");
setErrorHeader("UNKNOWN_ERROR");

$username = "";
$passwd = "";
require_once("config.php");
require_once("MyDB.php");

//check if the Headerfield action is set and correct
checkAction("LOGIN");



//check if the Data is ok
if(isset($_POST[getHeaderName("USERNAME")]) && isset($_POST[getHeaderName("PASSWORD")])){
    $username =$_POST[getHeaderName("USERNAME")];
    $passwd = $_POST[getHeaderName("PASSWORD")];
}else{
    error("EMPTY_FIELD");
}

$mydb = new MyDB();
$sql = "SELECT * FROM users2 WHERE name = ? and encrypted_password = ?";
$a = array();
$a[] = $username;
$a[] = $passwd;
$result = $mydb->query($sql,"ss",$a);

if(!$result){
    print_r($mydb->getError());
    error("DATABASE_ERROR");
}
if(sizeof($mydb->getRows()) != 1){
    error("NAME_PW_MISMATCH");
}

setErrorHeader("NO_ERROR");
?>