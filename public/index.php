<?php

define('ROUTE','../function/route/');
define('DB','../function/database.php');
define('USER','../function/user/user_query.php');

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

require ROUTE.'login.php';
require ROUTE.'register.php';
require ROUTE.'ingredient.php';

// this always last
require ROUTE.'any.php';

$app->run();

