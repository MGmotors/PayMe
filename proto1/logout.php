<?php
require_once("helper/functions.php");
session_start();
ensureLogin();
    
checkAction("LOGOUT");

setcookie(session_name(),'',0,'/');
session_unset();
session_destroy();
setErrorHeader("NO_ERROR")
?>