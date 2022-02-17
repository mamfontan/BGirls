<?php
    include_once('dbconfig.php');

    $device = '';

    getParameters();
    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            // Retrieve galleries
            if (empty($device)) {
                getLogins();
            } else {
                getLoginsFromDevice($device);
            }
            break;
        case 'POST':
            // Insert Product
            if (empty($device)) {
                echo "Parameters are empty";
            } else {
                insertLogin($device);
            }            
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }    

    function getLogins()
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT device, login, logout FROM logins ORDER BY login";
        
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}    

    function getLoginsFromDevice($device)
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT device, login, logout FROM logins WHERE device = '" . $device . "' ORDER BY login";
        
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}       

    function insertLogin($device)
    {
        insertDevice();

        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "INSERT INTO logins (device, login) VALUES ($device, CURRENT_TIMESTAMP)";
		$response = array();
		$response = mysqli_query($link, $query);

		header('Content-Type: application/json');
		echo json_encode($response);        
    }

    function insertDevice($device)
    {
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

        try {
            $query = "INSERT INTO devices (id) VALUES ($device)";
            $response = mysqli_query($link, $query);        
        } catch (Exception $ex) { }        
    }


    function getParameters()
    {
        global $device;

        if(isset($_POST['device'])) {
            $year = $_REQUEST['device'];
        }
    }    
?>