<?php
    include_once('dbconfig.php');
    include_once('model.php');

    $id = -1;
    $page = 0;
    $order = 0;

    getParameters();

    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            // Retrieve galleries
            getGalleries($page, $order);
            break;
        case 'POST':
            updateGalleryViews($id);
            break;
        case 'PUT':
            break;
        case 'DELETE':
            deleteGallery($id);
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }    

    function getGalleries($page, $order)
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$queryGallery = "SELECT id, name, image, views, rating FROM galleries ORDER BY " . $order . " DESC LIMIT " . $page * pageSize . ", " . pageSize;
        
		$result = mysqli_query($link, $queryGallery);

        $galleries = array();
		while($row = mysqli_fetch_assoc($result))
		{
            $newGallery = new Gallery();

            $newGallery->id = $row['id'];
            $newGallery->name = $row['name'];
            $newGallery->image = $row['image'];
            $newGallery->views = $row['views'];
            $newGallery->rating = $row['rating'];
            $newGallery->pics = array();

            array_push($galleries, $newGallery);
		}

        // Ahora recuperamos las fotos de cada galería
        foreach ($galleries as $item) {
            $queryPics = "SELECT url FROM pics WHERE idGallery = " . $item->id;

            $responsePics = array();
            $resultPics = mysqli_query($link, $queryPics);

            while($row = mysqli_fetch_assoc($resultPics))
            {
                array_push($responsePics, $row['url']);
            }

            $item->pics = $responsePics;
        }        

		header('Content-Type: application/json');
		echo json_encode($galleries);
	}    

    function updateGalleryViews($id)
    {
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$queryGallery = "SELECT views, rating FROM galleries WHERE id = " . $id;
		$result = mysqli_query($link, $queryGallery);    

        while($row = mysqli_fetch_assoc($result))
        {
            $actualViews = $row['views'];
        }    

        $updateQuery = "UPDATE galleries SET views = " . ($actualViews + 1) . " WHERE id = " . $id;
        $result = mysqli_query($link, $updateQuery);
        echo json_encode($result);
    }

    function deleteGallery($id)
    {
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "DELETE FROM galleries WHERE id = " . $id;
		$result = mysqli_query($link, $query);

		header('Content-Type: application/json');
		echo json_encode($result);        
    }

    function getParameters()
    {
        global $page;
        global $id;
        global $order;

        if(isset($_GET['page'])) {
            $page = $_REQUEST['page'];
        }
        else {
            $page = 0;
        }

        if(isset($_GET['order'])) {
            $order = $_REQUEST['order'];
        }
        else {
            $order = 0;
        }        

        if(isset($_POST['id'])) {
            $id = $_POST['id'];
        }
  
    }    

?>