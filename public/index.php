<?php

// define path names
define('ROOT',dirname(dirname(__FILE__)));
define ('FUN',ROOT.'/function');
define('ROUTE',FUN.'/route');
define('DB',FUN.'/database.php');
define('QUERY',FUN.'/query');
define('USER',QUERY.'/user_query.php');
define('INGREDIENT',QUERY.'/ingredient_query.php');

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;
require '../vendor/autoload.php';


$configuration = [
    'settings' => [
        'displayErrorDetails' => true,
	'debug'=>true,
    ],
]
;
$c = new \Slim\Container($configuration);
$app = new \Slim\App($c);

require ROUTE.'/login.php';
require ROUTE.'/register.php';
require ROUTE.'/ingredients_add.php';

// this always last
require ROUTE.'/any.php';
//
$app->run();

