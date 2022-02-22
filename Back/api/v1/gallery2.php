<?php
    include_once('dbconfig.php');

    $page = '';

    getParameters();

    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            // Retrieve galleries
            getGalleries($page);
            break;
        case 'POST':
            break;
        case 'PUT':
            break;
        case 'DELETE':
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }    

    function getGalleries()
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT id, name, url, views, rating FROM galleries ORDER BY id DESC LIMIT " . $page * pageSize . ", " . pageSize;
        
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}    

    function getParameters()
    {
        global $page;

        if(isset($_GET['page'])) {
            $year = $_REQUEST['page'];
        }
    }    

?>