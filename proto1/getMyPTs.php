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
$query = "SELECT * FROM debtor, payme WHERE debtor.user_id = ? AND debtor.payme_id = payme.id";
$params = array($id);
$myPTs = sendQuery($query, $params);


$print = "{\"items\":[ ";
for($i = 0; $i<sizeof($myPTs);$i++){
    $row = $myPTs[$i];
    $s = 
    "{  \"creator\": ".getUsername($row["user_id"]).",
        \"title\": \"".$row["title"]."\",
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