<?php

    $request_method = $_SERVER["REQUEST_METHOD"];
    switch($request_method)
    {
        case 'GET':
            header('Content-Type: application/json');
            $response = array('Server is online');
            echo json_encode($response);
            break;
        default:
            // Invalid Request Method
            header("HTTP/1.0 405 Method Not Allowed");
            break;
    }
?>