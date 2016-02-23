<?php 
/*
Returns a list of PM's created by a specific user specified by his email
*/
require_once("helper/functions.php");
setErrorHeader("UNKNOWN_ERROR");

session_start();
ensureLogin();

//check if the Headerfield action is set and correct
checkAction("GET_MY_PMS");




$id= $_SESSION["uid"];
$myPMs = getMyPMs($id);
$print = "{[ ";
for($i = 0; $i<sizeof($myPMs);$i++){
    $row = $myPMs[$i];
    $s = 
    "{  'name': '".$row["name"]."' 
        'decription' : '".$row["description"]."'    
        'debtors' : ".getDebtorNamesAndStateInJSON($row["id"])."
        'price' : ".$row["price"]."
    }";
    if(($i+1) < sizeof($myPMs)){
        $s .= ",";
    }
    $print .= $s;
}
$print .= " ]}";

print($print);
setErrorHeader("NO_ERROR");


#################### Functions #######################
######################################################

function getMyPMs($id){
    try{
        $con = new PDO( DB_DSN, DB_USER, DB_PASSWORD );
        $con->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_SILENT );
        $sql = "SELECT * FROM payme WHERE user_id = :id";
        $stmt = $con->prepare($sql);
        $stmt->bindValue( "id", $id);
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
        $str.=  "  {'name': '" .$row["username"] ."','haspayed' : '".$row["has_payed"]."' } \r";
        if(($i+1)<sizeof($result)){
            $str.=",";
        }
    }
    
    $str .= "]";
    
    return $str;
}



?>