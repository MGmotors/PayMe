<?php
require_once("config.php");

function setErrorHeader($code){
    header(getHeaderName("ERROR"). ":" . getErrorCode($code) );
}

function error($type){
    setErrorHeader($type);
    die();
}


function debug($msg){
    if($isdebug){
        print("debug: " .  $msg . "\n");
    }
}
function checkAction($action){
    $expected = getActionCode($action);
    $headerName = getHeaderName("ACTION");
    
    if(isset($_POST[$headerName])){
        if($_POST[$headerName] != $expected){
            error("ACTIONS_MISMATCH");
        }
    }else{
        error("ACTIONS_MISMATCH");
    }
}

function getHeaderName($str){
    return API::$HeaderFields[$str];
}
function getErrorCode($str){
    return API::$ErrorCodes[$str];
}
function getActionCode($str){
   return API::$ActionCodes[$str];
}

?>