<?php 
/*
Returns a list of PM's created by a specific user specified by his email
*/
require_once("helper/functions.php");
setErrorHeader("UNKNOWN_ERROR");

session_start();
ensureLogin();

//check if the Headerfield action is set and correct
checkAction("GET_MY_PTS");


$id = $_SESSION["uid"];
$myPTs = getMyPTs($id);
$print = "{\"items\":[ ";
for($i = 0; $i<sizeof($myPTs);$i++){
    $row = $myPTs[$i];
    $s = 
    "{  \"creator\": ".getName($row["user_id"]).",
        \"title\": \"".$row["name"]."\",
        \"description\" : \"".$row["description"]."\",
        \"debtors\" : ".getDebtorNamesAndStateInJSON($row["id"]).",
        \"price\" : ".$row["price"].",
        \"datetime\" : \"" .$row["datetime"] . "\"
    }";
    if(($i+1) < sizeof($myPTs)){
        $s .= ",";
    }
    $print .= $s;
}
$print .= " ]}";

print($print);
setErrorHeader("NO_ERROR");
?>