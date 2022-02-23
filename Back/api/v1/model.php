<?php
    class Gallery {
        public $id;
        public $name;
        public $image;
        public $pics;
        public $views;
        public $rating;

        public function __construct() {
            $id = 0;
            $name = '';
            $image = '';
            $pics = array();
            $views = 0;
            $rating = 0;
        }        
    }

    class Video {
        public $id;
        public $name;
        public $url;
        public $views;
        public $rating;

        public function __construct() {
            $id = 0;
            $name = '';
            $url = '';
            $views = 0;
            $rating = 0;
        }        
    }    
?>
