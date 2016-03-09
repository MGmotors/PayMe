<?php

require_once("helper/functions.php");
session_start();
ensureLogin();
checkAction("REGISTER_GCM_TOKEN");


$token = getFieldOrDie("GCM_TOKEN");
if(!$token)  {
    error("BAD_DATA");
}

$id = $_SESSION["uid"];
$query = "UPDATE users SET gcm_token = ? WHERE id = ?";
$params = array($token, $id);
selectQuery($query, $params);

setErrorHeader("NO_ERROR");

?>