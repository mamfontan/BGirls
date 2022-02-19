<?php
    include_once('dbconfig.php');

    $year = '';
    $month = '';
    $name = '';
    $url = '';

    getParameters();

    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            // Retrieve galleries
            if(empty($year) || empty($month)) {
                getGalleries();
            } else {
                getGallery($year, $month);
            }
            break;
        case 'POST':
            // Insert Product
            if(empty($year) || empty($month)) {
                echo "Parameters are empty";
            } else {
                insertGallery($year, $month, $name, $url);
            }            
            break;
        case 'PUT':
            // Update Product
            $product_id=intval($_GET["product_id"]);
            update_product($product_id);
            break;
        case 'DELETE':
            // Delete Product
            $product_id=intval($_GET["product_id"]);
            delete_product($product_id);
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }

    function getParameters()
    {
        global $year;
        global $month;
        global $name;
        global $url;

        if(isset($_GET['year'])) {
            $year = $_REQUEST['year'];
        }
    
        if(isset($_GET['month'])) {
            $month = $_REQUEST['month'];
        }
    
        if(isset($_GET['name'])) {
            $name = $_REQUEST['name'];
        }
        
        if(isset($_GET['url'])) {
            $url = $_REQUEST['url'];
        }           
    }

    function getGalleries()
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT year, month, name, url, views, rating FROM galleries ORDER BY year, month";
        
		$response = array();
		$result = mysqli_query($link, $query);

		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}

    function getGallery($year, $month)
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT year, month, name, url, views, rating FROM galleries WHERE year = " . $year . " AND month = " . $month;
        
		$response = array();
		$result = mysqli_query($link, $query);
		while($row = mysqli_fetch_assoc($result))
		{
			$response[] = $row;
		}

		header('Content-Type: application/json');
		echo json_encode($response);
	}    

    function insertGallery($year, $month, $name, $url)
    {

    }

    function updateGallery($year, $month, $name, $url)
    {

    }

    function deleteGalleries($year, $month, $name)
    {

    }
?>