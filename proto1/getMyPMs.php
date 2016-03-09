<?php 
/*
Returns a list of PM's created by a specific user specified by his email
*/
require_once("helper/functions.php");
setErrorHeader("UNKNOWN_ERROR");

session_start();
ensureLogin();

//check if the Headerfield action is set and correct
checkAction("GET_MY_PMS");


$id = $_SESSION["uid"];
$query = "SELECT * FROM payme WHERE user_id = ?";
$params = array($id);
$myPMs = selectQuery($query, $params);


$print = "{\"items\":[ ";
for($i = 0; $i<sizeof($myPMs);$i++){
    $row = $myPMs[$i];
    $s = 
    "{  \"creator\": ".getUsername($row["user_id"]).",
        \"title\": \"".$row["title"]."\", 
        \"description\" : \"".$row["description"]."\",    
        \"debtors\" : ".getDebtorNamesAndStateInJSON($row["id"]).",
        \"price\" : ".$row["price"].",
        \"datetime\" : \"" .$row["datetime"] . "\"
    }";
    if(($i+1) < sizeof($myPMs)){
        $s .= ",";
    }
    $print .= $s;
}
$print .= " ]}";

print($print);
setErrorHeader("NO_ERROR");
?>