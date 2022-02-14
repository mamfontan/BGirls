<?php
    // Developement configuration
    define('dbHost', 'localhost');
    define('dbName', 'kindgirls');
    define('dbUser', 'kguser');
    define('dbPass', 'lBAy3R07Zfrc0ut7');

    function countTableElements($tableName)
    {
        try {
            $conn = new mysqli(dbHost, dbUser, dbPass, dbName);
            
            // Check connection
            if ($conn->connect_error) {
              echo die("Connection failed: " . $conn->connect_error);
            }
            
            $sql = "SELECT count(*) as total from " . $tableName;
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_assoc($result);
            $total = $row['total'];
 
            $conn->close();
            return $total;
            
        } catch(Exception $e) {
            return -1;
        }  
    }

?>