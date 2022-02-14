<?php
    ini_set('max_execution_time', 4500);
    include_once('dbconfig.php');

    $initialYear = date("Y");
    $initialMonth = 1;

    $actualYear = date("Y");
    $actualMonth = date("m");

    $picsUrl = 'https://www.kindgirls.com/photo-archive.php?s=' . $actualMonth . '-' . $actualYear;
    $videoUrl = 'https://www.kindgirls.com/video-archive.php?p=1';

    for ($year = $initialYear; $year <= $actualYear; $year++) {
        for ($month = $initialMonth; $month <= 12; $month++) {

            // read web page
            getGalleriesWebPage($year, $month);
            $galleries = getGalleryLinesFromWebPage($year, $month);
            insertGalleriesInDataBase($galleries);

            $galleriesCount = count($galleries);
            for ($x = 0; $x < $galleriesCount; $x++)
            {
                $galleryPics = getGalleryPics($galleries[$x]);
                insertPicsInDatabase($galleryPics);
            } 

            $outfile = "webPage" . $year . "-" .$month . ".html";
            deleteFile($outfile);
        }
    }

    function getGalleriesWebPage($year, $month)
    {
        $url = "https://www.kindgirls.com/photo-archive.php?s=" . $month . "-" . $year;

        $page = file_get_contents($url);
        $outfile = "webPage" . $year . "-" .$month . ".html";
        file_put_contents($outfile, $page);
    }

    function getGalleryLinesFromWebPage($year, $month)
    {
        $galleries = [];
        $sourceFile = 'webPage' . $year . "-" .$month . '.html';

        if ($file = fopen($sourceFile, 'r')) {
            while(!feof($file)) {
                $fullGalleryLine = fgets($file);

                if (strpos($fullGalleryLine, '<div class="gal_list">') === 0) {
                    // Eliminamos las partes innecesarias
                    $shortGalleryLine = str_replace('<div class="gal_list">', '', $fullGalleryLine);
                    $shortGalleryLine = str_replace('</div>', '', $shortGalleryLine);

                    // Buscamos la posicion del enlace
                    $galleryLink = null;
                    $imageLink = null;

                    $iniPosition = strpos($shortGalleryLine, 'href="', 0);
                    if ($iniPosition)
                    {
                        $endPosition = strpos($shortGalleryLine, '">', $iniPosition);
                        $galleryLink = 'https://www.kindgirls.com' . substr($shortGalleryLine, $iniPosition + 6, $endPosition - $iniPosition - 6);
                    }

                    // Buscamos la posicion de la imagen
                    $iniPosition = strpos($shortGalleryLine, 'img src="', 0);
                    if ($iniPosition)
                    {
                        $endPosition = strpos($shortGalleryLine, '" alt=', $iniPosition);
                        $imageLink = substr($shortGalleryLine, $iniPosition + 9, ($endPosition - $iniPosition) - 9);
                    }

                    // Buscamos la posicion del nombre
                    $iniPosition = strpos($shortGalleryLine, ' alt="', 0);
                    if ($iniPosition)                     
                    {
                        $endPosition = strpos($shortGalleryLine, ' pics', $iniPosition);   
                        $name = substr($shortGalleryLine, $iniPosition + 6, ($endPosition - $iniPosition) - 6 - 4);
                    }

                    $newGallery = new Gallery();
                    $newGallery->year = $year;
                    $newGallery->month = $month;
                    $newGallery->name = $name;
                    $newGallery->url = $galleryLink;
                    $newGallery->image = $imageLink;

                    array_push($galleries, $newGallery);
                }

             }
            fclose($file);
        }  

        return $galleries;
    }

    function insertGalleriesInDataBase($galleries)
    {
        try {
            $conn = new mysqli(dbHost, dbUser, dbPass, dbName);
            // Check connection
            if ($conn->connect_error) {
              echo die("Connection failed: " . $conn->connect_error);
            }

            $sql = 'INSERT INTO galleries (year, month, name, url, image) VALUES ';

            $numElements = count($galleries);
            for ($x = 0; $x < $numElements; $x++)
            {
                $sql = $sql . '('. $galleries[$x]->year .', '. $galleries[$x]->month .', "' . $galleries[$x]->name .'", "'.$galleries[$x]->url.'", "'.$galleries[$x]->image.'")';
                if ($x < $numElements-1)
                {
                    $sql = $sql . ', ';
                }
            }              

            mysqli_query($conn, $sql);
            $conn->close();
        } catch(Exception $e) {
            echo "Error inserting gallery";
        }  
    }

    function insertPicsInDatabase($pics)
    {
        try {
            $conn = new mysqli(dbHost, dbUser, dbPass, dbName);
            // Check connection
            if ($conn->connect_error) {
              echo die("Connection failed: " . $conn->connect_error);
            }

            $sql = 'INSERT INTO pics (idGallery, name, url) VALUES ';

            $numElements = count($pics);
            for ($x = 0; $x < $numElements; $x++)
            {
                $sql = $sql . '('. $pics[$x]->idGallery .', "' . $pics[$x]->name. '", "'.$pics[$x]->url.'")';
                if ($x < $numElements-1)
                {
                    $sql = $sql . ', ';
                }
            }

            mysqli_query($conn, $sql);
            $conn->close();
        } catch(Exception $e) {
            echo "Error inserting pics";
        }  

    }

    function deleteFile($fileName)
    {
        $file_pointer = $fileName; 
        unlink($file_pointer);
    }

    function getGalleryPics($gallery)
    {
        getPicsWebPage($gallery);

        $pics = [];
        $idGallery = getIdGallery($gallery);

        $sourceFile = "galleryPics - " .$gallery->name . ".html";
        if ($file = fopen($sourceFile, 'r')) {
            while(!feof($file)) {
                $fullPicsLine = fgets($file);

                if (strpos($fullPicsLine, '<div class="gal_list">') === 0) {

                    $shortPicsLine = str_replace('<div class="gal_list">', '', $fullPicsLine);
                    $shortPicsLine = str_replace('</div>', '', $shortPicsLine);

                    // Buscamos el enlace de la imagen
                    $iniPosition = strpos($shortPicsLine, 'href="', 0);
                    if ($iniPosition)
                    {
                        $endPosition = strpos($shortPicsLine, '">', $iniPosition);
                        $imageLink = 'https://www.kindgirls.com/' . substr($shortPicsLine, $iniPosition + 6, ($endPosition - $iniPosition) - 6);
                        $imageLink = str_replace('amp;', '', $imageLink);
                    }

                    // Buscamos el nombre de l imagen
                    $iniPosition = strpos($shortPicsLine, 'title="', 0);
                    if ($iniPosition)
                    {
                        $endPosition = strpos($shortPicsLine, '" alt', $iniPosition);
                        $imageName = substr($shortPicsLine, $iniPosition + 7, ($endPosition - $iniPosition) - 7);
                    }                    

                    $newPic = new Pic();
                    $newPic->idGallery = $idGallery;
                    $newPic->name = $imageName;
                    $newPic->url = $imageLink;

                    array_push($pics, $newPic);
                }
            }
        }

        // TODO Comentado para pruebas
        deleteFile("galleryPics - " .$gallery->name . ".html");

        return $pics;
    }    

    function getPicsWebPage($gallery)
    {
        $page = file_get_contents($gallery->url);
        $outfile = "galleryPics - " .$gallery->name . ".html";
        file_put_contents($outfile, $page);
    }    
    
    function getIdGallery($gallery)
    {

        try {
            $conn = new mysqli(dbHost, dbUser, dbPass, dbName);
            
            // Check connection
            if ($conn->connect_error) {
              echo die("Connection failed: " . $conn->connect_error);
            }
            
            $sql = 'SELECT id FROM galleries WHERE year = ' . $gallery->year . ' AND month = ' . $gallery->month . ' AND url = "' . $gallery->url . '"';
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_assoc($result);
            $id = $row['id'];
 
            $conn->close();
            return $id;
            
        } catch(Exception $e) {
            return -1;
        }  

    }

    class Gallery {
        public $year;
        public $month;
        public $name;
        public $url;
        public $image;
    }

    class Pic {
        public $idGallery;
        public $name;
        public $url;
    }

?>