<?php


class MyDB{
    private $error = "";
    private $responseRows = null;
    
    const DB_HOST =  "localhost";
    const DB_USER = "schabimperle";
    const DB_PASSWORD = "";
    const DB_DATABASE = "pay_me";
    
    //dataTypes is like "sss" if i want to insert 3 Strings
    function query($sql, $dataTypes, $aData){
        $conn = new mysqli(self::DB_HOST, self::DB_USER, self::DB_PASSWORD, self::DB_DATABASE);
        if($conn->connect_errno > 0){
            $error='Unable to connect to database [' . $conn->connect_error . ']';
            return false;
        }
        
        $this->responseRows = array();
        /* Bind parameters. Types: s = string, i = integer, d = double,  b = blob */
        $a_params = array();
       
        //was ist diese komische Schreibweise?
        $a_params[] = & $dataTypes;
       
        for($i = 0; $i < count($aData); $i++) {
            /* with call_user_func_array, array params must be passed by reference */
            $a_params[] = & $aData[$i];
        }
 
        /* Prepare statement */
        $stmt = $conn->prepare($sql);
        
        if($stmt === false) {
            $this->error = 'Wrong SQL: ' . $sql . ' Error: ' . $conn->errno . ' ' . $conn->error;
            return false;
        }
 
        /* use call_user_func_array, as $stmt->bind_param('s', $param); does not accept params array */
        call_user_func_array(array($stmt, 'bind_param'), $a_params);
 
        /* Execute statement */
       if(!($stmt->execute())){
            $this->error="Execute failed";
            return false;
       }
 
        /* Fetch result to array */
        $res = $stmt->get_result();
        if($res){
            while($row = $res->fetch_array(MYSQLI_ASSOC)) {
                array_push($this->responseRows, $row);
            }
        }
        
        return true;
        
        //eventuell schließen ;)
    } 
    
    function getError(){
        return  $this->error;
    }
    
    function getRows(){
        return $this->responseRows;
    }
}
?>