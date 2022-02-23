<?php
    include_once('dbconfig.php');
    include_once('model.php');

    $id = -1;
    $page = 0;

    getParameters();

    $request_method = $_SERVER["REQUEST_METHOD"];
    
    switch($request_method)
    {
        case 'GET':
            // Retrieve galleries
            getVideos($page);
            break;
        case 'POST':
            break;
        case 'PUT':
            break;
        case 'DELETE':
            deleteVideo($id);
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }

    function getVideos($page)
	{
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "SELECT id, name, url, views, rating FROM videos ORDER BY id DESC LIMIT " . $page * pageSize . ", " . pageSize;
        
		$result = mysqli_query($link, $query);

        $videos = array();
		while($row = mysqli_fetch_assoc($result))
		{
            $newVideo = new Video();

            $newVideo->id = $row['id'];
            $newVideo->name = $row['name'];
            $newVideo->url = $row['url'];
            $newVideo->views = $row['views'];
            $newVideo->rating = $row['rating'];

            array_push($videos, $newVideo);
		}

		header('Content-Type: application/json');
		echo json_encode($videos);
	}    
    
    function deleteVideo($id)
    {
        $link = mysqli_connect(dbHost, dbUser, dbPass, dbName);

        // Check connection
        if ($link === false) {
            die("ERROR: Could not connect. " . mysqli_connect_error());
        }

		$query = "DELETE FROM videos WHERE id = " . $id;
		$result = mysqli_query($link, $query);

		header('Content-Type: application/json');
		echo json_encode($result);
    }

    function getParameters()
    {
        global $page;

        if(isset($_GET['page'])) {
            $page = $_REQUEST['page'];
        }
        else {
            $page = 0;
        }

        if(isset($_GET['id'])) {
            $id = $_REQUEST['id'];
        }
  
    }       


?>