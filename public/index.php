<?php

// define path names
define('ROOT',dirname(dirname(__FILE__)));
define ('FUN',ROOT.'/function');
define('ROUTE',FUN.'/route');
define('DB',FUN.'/database.php');
define('QUERY',FUN.'/query');
define('USER',QUERY.'/user_query.php');
define('INGREDIENT',QUERY.'/ingredient_query.php');
define('USER_INGREDIENT',QUERY.'/user_ingredient_functions.php');


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;
use \Exception ;

require '../vendor/autoload.php';

$configuration = ['settings' => [
        'displayErrorDetails' => true, 'debug'=>true,],];

$c = new \Slim\Container($configuration);

$app = new \Slim\App($c);

function initialize(){
    require_once  USER;
    require_once INGREDIENT;
    require_once USER_INGREDIENT;
}

require ROUTE.'/login.php';
require ROUTE.'/register.php';
require ROUTE.'/ingredients_add.php';
require ROUTE.'/ingredients_return.php';

// this always last
require ROUTE.'/any.php';
//
$app->run();

