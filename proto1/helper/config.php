<?php
    define("DB_HOST", "localhost");
    define("DB_USER", "schabimperle");
    define("DB_DSN", "mysql:host=localhost;dbname=pay_me" );
    define("DB_PASSWORD", "");
    define("DB_DATABASE", "pay_me");
    
    define("DB_ERROR_LOG","/home/ubuntu/workspace/logs/dbErrors.log");
    
    $file = "/home/ubuntu/workspace/API/codes.json";
    $myfile = fopen($file, "r") or die("Unable to open file!");
    $API_json = json_decode(fread($myfile,filesize($file)),true);
    fclose($myfile);
    class API{
        static $ActionCodes;
        static $ErrorCodes;
        static $HeaderFields;
        static $URLs;
    }
    API::$ActionCodes = $API_json["ActionCodes"];
    API::$ErrorCodes = $API_json["ErrorCodes"];
    API::$HeaderFields = $API_json["HeaderFields"];
    API::$URLs = $API_json["URLs"];
    
?>
