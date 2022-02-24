<?php
    // Developement configuration
    define('dbHost', 'localhost');
    define('dbName', 'id18175334_ggalleries');
    define('dbUser', 'id18175334_ggalleries_user');
    define('dbPass', 'l#o0{JjQ1R=/f_zG');

    define('pageSize', 30);

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