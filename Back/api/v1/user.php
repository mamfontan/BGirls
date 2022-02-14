<?php
    include_once('dbconfig.php');

	$email = null;
	$password = null;

	getParameters();

    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            // Retrieve users
			if  (is_null($email) && is_null($password)) 
			{			
				getUsers();
			} else {
				getUser($email, $password);
			}
            break;
        case 'POST':
            // Insert user
            break;
        case 'PUT':
            // Update user
            break;
        case 'DELETE':
            // Delete user
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }	

    function getParameters()
    {
        global $email;
        global $password;

        if(isset($_GET['email'])) {
            $email = $_REQUEST['email'];
        }
    
        if(isset($_GET['password'])) {
            $password = $_REQUEST['password'];
        }
    }	

    function getUsers()
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT id, email, password, active FROM users ORDER BY email";
       
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}

    function getUser($email, $password)
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT id, email, password, active FROM users WHERE email = '" . $email . "' AND password = '" . $password . "' AND active = 1";
       
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}	

    function insertUser()
    {

    }

    function updateUser()
    {

    }

    function deleteUser()
    {

    }
?>