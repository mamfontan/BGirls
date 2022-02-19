<?php
    include_once('dbconfig.php');

    $device = '';

    getParameters();
    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'POST':
            // Insert Product
            if (!empty($device)) {
                insertLogout($device);
            }            
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }    

    function insertLogout($device)
    {
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "UPDATE logins SET logout = CURRENT_TIMESTAMP WHERE device = '" . $device . "' AND logout is null";
		$response = array();
		$response = mysqli_query($link, $query);

		header('Content-Type: application/json');
		echo json_encode($response);          
    }

    function getParameters()
    {
        global $device;

        if(isset($_POST['device'])) {
            $device = $_POST['device'];
        }
    }        

?>