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

function selectQuery($query, $params) {
     try{
        $con = new PDO(DB_DSN, DB_USER, DB_PASSWORD);
        $con -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT);
        $stmt = $con -> prepare($query);
        $succ = $stmt -> execute($params);
        if(!$succ){
            $arr = $stmt -> errorInfo();
            dbError($arr[2]);
        }
        return $stmt -> fetchAll();
    } catch(PDOException $e) {
       dbError($e->getMessage());
    }
}

function getUsername($uid) {
        $query = "SELECT username FROM users WHERE id = ?";
        $params = array($uid);
        $result = selectQuery($query, $params);
        return $result[0]["username"];
}

function getDebtorNamesAndStateInJSON($paymeID){
     $query = "SELECT * FROM debtor, users WHERE debtor.payme_id = ? AND debtor.user_id = users.id";
     $params = array($paymeID);
     $result = selectQuery($query, $params);
    
    $str = "[";
    
    for($i= 0; $i<sizeof($result);$i++){
        $row = $result[$i];
        $str.=  "  {\"username\" : \"" .$row["username"] ."\",\"haspayed\" : ".$row["has_payed"]." } \r";
        if(($i+1)<sizeof($result)){
            $str.=",";
        }
    }
    
    $str .= "]";
    
    return $str;
}

function getGcmToken($id) {
    $query = "SELECT gcm_token FROM users WHERE id = ?";
    $params = array($id);
    $result = selectQuery($query, $params);
    $token = $result[0][0];
    if(!$token) {
        error("BAD_DATA");
    }
    return $token;
}

function sendGcmMessage($ids, $params) {

    //prepare message to send to google server
    $data = "{ \"to\" : \"" . $token . "\" }";
    $url = "https://gcm-http.googleapis.com/gcm/send";
    $header1 = "Authorization: key=AIzaSyCdmU7GKm0XPTJ9IBjXa4x_uuFEudW819k";
    $header2 = "Content-Type: application/json";
    $header3 = "Content-Length: " . strlen($data);
    
    $opts = array(
        'http' => array (
            'method' => "POST",
            'ignore_errors' => true,
            'header' => $header1 . "\r\n" .
                        $header2 . "\r\n" .
                        $header3 . "\r\n",
            'content' => $data
        )
    );
    
    //send message to google server
    $context = stream_context_create($opts);
    $fp = fopen($url, 'r', false, $context);
    
    //write answer from google server
    // if($fp != false) {
    //     fpassthru($fp);
    //     fclose($fp);
    // }
    
    return $fp;
}

?>