<?php
// define path names
define('ROOT',dirname(dirname(__FILE__)));
define ('FUN',ROOT.'/function');
define ('IMAGE',ROOT.'/image');
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

// config files has error handler that converts php errors to json
$configuration =include_once  FUN.'/config.php';

$c = new \Slim\Container($configuration);

$app = new \Slim\App($c);

// include our querys
foreach (glob(QUERY.'/*.php') as $filename)
{
    include $filename;
}

// include routes 
foreach (glob(ROUTE.'/*.php') as $filename)
{
    include $filename;
}

// this always last
include 'any.php';

$app->run();

