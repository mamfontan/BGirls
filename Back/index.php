<?php
    include_once('api/v1/dbconfig.php');
?>
<!DOCTYPE html>
<head>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>

    <!-- Google fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Gluten:wght@600&family=Kanit&display=swap" rel="stylesheet">

    <!-- local styles -->
    <link rel="stylesheet" type="text/css" href="index.css" media=”screen”>
    <meta name = "keywords" content = "HTML, Meta Tags, Metadata" />  
    <title>Kind Girls</title>
</head>
<html>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-sm-12 col-md-12 text-center">
                    <div class="title">Kind Girls</div>
                </div>
                <div class="col-sm-12 col-md-12 text-center">
                    This is a clean and honest site about beautiful nude girls. We hope you enjoy kindgirls.<br/>
                    Do not forget to have a look at our free Photo Archive: 124766 photos in 9151 galleries, growing daily (or almost).
                </div>                
            </div>
            <div class="row">
                <div class="col-sm-3 col-md-3 text-center">Uno</div>
                <div class="col-sm-3 col-md-3 text-center">Dos</div>
                <div class="col-sm-3 col-md-3 text-center">Tres</div>
                <div class="col-sm-3 col-md-3 text-center">Cuatro</div>
            </div>
            <hr>
            <div class="row">
                <div class="col-sm-4 col-md-4 text-center">Galleries: <?php echo countTableElements('galleries'); ?></div>
                <div class="col-sm-4 col-md-4 text-center">Pics: <?php echo countTableElements('pics'); ?></div>
                <div class="col-sm-4 col-md-4 text-center">Videos: <?php echo countTableElements('videos'); ?></div>
            </div>
            <div class="row" style="margin-top:25px">
                <div class="col-sm-12 col-md-12 text-center">Download apk <a href="app-debug.zip">Beauty girls</a></div>
            </div>

        </div>
    </body>
</html>
