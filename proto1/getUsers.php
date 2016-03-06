<?php 

require_once("helper/functions.php");
setErrorHeader("UNKNOWN_ERROR");

session_start();
ensureLogin();

checkAction("GET_USERS");

$query = "SELECT username FROM users WHERE username LIKE ?";
$params = array(getFieldOrDie("SEARCH_USER")."%");
$users = sendQuery($query, $params);

for($i = 0; $i < sizeof($users); $i++) {
    $result .= $users[$i][0]."\n";
}
print($result);

setErrorHeader("NO_ERROR");
?>