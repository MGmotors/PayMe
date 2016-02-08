<?php
    $myfile = fopen("codes.json", "r") or die("Unable to open file!");
    $json = json_decode(fread($myfile,filesize("codes.json")),true);
    fclose($myfile);
    class Constants{
        static $ActionCodes;
        static $ErrorCodes;
        static $HeaderFields;
        static $URLs;
    }
    Constants::$ActionCodes = $json["ActionCodes"];
    Constants::$ActionCodes = $json["ErrorCodes"];
    Constants::$ActionCodes = $json["HeaderFields"];
    Constants::$ActionCodes = $json["URLs"];

      x();
    function x(){
         print_r(Constants::$ActionCodes["LOGIN"]);
    }

?>