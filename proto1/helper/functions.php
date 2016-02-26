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

function error($type, $description = ""){
    if($description != ""){
        if((DEBUG_LEVEL & 0b01)>0){
            file_put_contents(SERVER_LOG,$description . \n,FILE_APPEND);
        }
        if((DEBUG_LEVEL & 0b10)>0){
            print($description . \n);
        }
    }
    setErrorHeader($type);
    die();
}

function dbError($sting){
     file_put_contents(DB_ERROR_LOG, $sting . "\n\n", FILE_APPEND );
     error("DATABASE_ERROR");
}

function checkAction($action){
    $expected = getActionCode($action);
    $headerName = getHeaderName("ACTION");
    if(isset($_POST[$headerName])){
        if($_POST[$headerName] != $expected){
            error("ACTIONS_MISMATCH");
        }
        header($headerName . ":". $expected);
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