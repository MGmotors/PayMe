<?php

$isdebug = false;

if(isset($_POST["payme-debug"])){
    if(strtolower($_POST["payme-debug"]) == "true" ){
        $isdebug = true;
    }    
}


function error($type, $msg){
    header('payme-error: ' . $type);
    header('payme-error_description: ' .  trim(preg_replace('/\s+/', ' ', $msg)));
    die("Error \n");
}


function debug($msg){
    if(isdebug){
        print("debug: " .  $msg . "\n");
    }
}

function checkAction($expected){
    if(isset($_POST["payme-action"])){
        if($_POST["payme-action"] != $expected){
            error("action", "The actions do not match. Required: " . $expected . " sent: " . $_POST["payme-action"] );
        }
    }else{
        error("action", "The action was not specified");
    }
}

?>