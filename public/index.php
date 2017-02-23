<?php

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;
require '../vendor/autoload.php';


$configuration = [
    'settings' => [
        'displayErrorDetails' => true,
    ],
];
$c = new \Slim\Container($configuration);
$app = new \Slim\App($c);

$app->get('/register/{val}', function (Request $request, Response $response) {
	require_once 'function/database.php';
	$db=db_connect();
	$name = json_encode(["HELLO"=>$request->getAttribute('val') ] );
    	$response->getBody()->write($name);;
	$response = $response->withHeader('Content-Type', 'application/json');
	db_close($db);
    	return $response;
});

$app->get('/', function (Request $req,  Response $res, $args = []) {
    return $res->withStatus(400)->write('Bad Request');
});

$app->run();

