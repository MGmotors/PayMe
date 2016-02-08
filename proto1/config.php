<?php
    define("DB_HOST", "localhost");
    define("DB_USER", "schabimperle");
    define("DB_PASSWORD", "");
    define("DB_DATABASE", "pay_me");
    
    $file = "../API/codes.json";
    $myfile = fopen($file, "r") or die("Unable to open file!");
    $json = json_decode(fread($myfile,filesize($file)),true);
    fclose($myfile);
    class API{
        static $ActionCodes;
        static $ErrorCodes;
        static $HeaderFields;
        static $URLs;
    }
    API::$ActionCodes = $json["ActionCodes"];
    API::$ErrorCodes = $json["ErrorCodes"];
    API::$HeaderFields = $json["HeaderFields"];
    API::$URLs = $json["URLs"];
    
?>
