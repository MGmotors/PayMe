<?php
require_once("config.php");

function getHeaderName($str){
    return API::$HeaderFields[$str];
}

function getErrorCode($str){
    return API::$ErrorCodes[$str];
}

function getActionCode($str){
   return API::$ActionCodes[$str];
}


function setErrorHeader($code){
    header(getHeaderName("ERROR"). ":" . getErrorCode($code) );
}

function error($type){
    setErrorHeader($type);
    die();
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

function ensureLogin(){
    if(!isset($_SESSION["uid"])){
        error("NOT_LOGGEDIN");
    }
}

function getFieldOrDie($field){
    if(isset($_POST[getHeaderName($field)])) {
        return $_POST[getHeaderName($field)];
    }else{
        error("EMPTY_FIELD");
    }
}


?>