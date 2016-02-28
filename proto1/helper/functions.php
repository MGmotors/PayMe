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

function getJSONCode($str) {
    return API::$JSON[$str];
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

function getName($uid) {
    try{
        $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
        $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
        $sql = "SELECT username FROM users where id = :id";
        $stmt = $con->prepare($sql);
        $stmt->bindValue( "id", $uid);
        $succ = $stmt->execute();
        if(!$succ){
            $arr = $stmt->errorInfo();
            dbError($arr[2]);
        }
        $result = $stmt->fetchAll();
        return $result[0]["username"];
    } catch(PDOException $e) {
       dbError($e->getMessage());
    }
}

function getMyPMs($uid){
    try{
        $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
        $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
        $sql = "SELECT * FROM payme WHERE user_id = :id";
        $stmt = $con->prepare($sql);
        $stmt->bindValue( "id", $uid);
        $succ = $stmt->execute();
        if(!$succ){
            $arr = $stmt->errorInfo();
            dbError($arr[2]);
        }
        return $stmt->fetchAll();
    } catch(PDOException $e) {
       dbError($e->getMessage());
    }
}

function getMyPTs($uid){
    try{
        $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
        $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
        $sql = "SELECT * FROM debtor, payme WHERE debtor.user_id = :id AND debtor.payme_id = payme.id";
        $stmt = $con->prepare($sql);
        $stmt->bindValue( "id", $uid);
        $succ = $stmt->execute();
        if(!$succ){
            $arr = $stmt->errorInfo();
            dbError($arr[2]);
        }
        return $stmt->fetchAll();
    } catch(PDOException $e) {
       dbError($e->getMessage());
    }
}

function getDebtorNamesAndStateInJSON($paymeID){
     try{
        $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
        $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
        $sql = "SELECT * FROM debtor, users WHERE debtor.payme_id = :id AND debtor.user_id = users.id";
        $stmt = $con->prepare($sql);
        $stmt->bindValue( "id", $paymeID);
        $succ = $stmt->execute();
        if(!$succ){
            $arr = $stmt->errorInfo();
            dbError($arr[2]);
        }
        $result = $stmt->fetchAll();
        //print_r($result);
    } catch(PDOException $e) {
        dbError($e->getMessage());
    }
    
    $str = "[";
    
    for($i= 0; $i<sizeof($result);$i++){
        $row = $result[$i];
        $str.=  "  {\"username\": \"" .$row["username"] ."\",\"haspayed\" : ".$row["has_payed"]." } \r";
        if(($i+1)<sizeof($result)){
            $str.=",";
        }
    }
    
    $str .= "]";
    
    return $str;
}
?>