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

$app->get('/register/{user}/{password}/{email}', function (Request $request, Response $response) {
	require_once 'function/database.php';
	$db=db_connect();
	//$name = json_encode(["USER"=>$request->getAttribute('user'), "PASSWORD" =>$request->getAttribute('password'),"Email" =>  $request->getAttribute('email')] );
    	//$response->getBody()->write($namei);

	$name= $request->getAttribute('user');	
	$password= sha1($request->getAttribute('password'));	
	$email= $request->getAttribute('email');	

	if(add_user($name,$password,$email,$db)){
		$out= json_encode(["Result" => "Added"]);
    		$response->getBody()->write($out);
	}
	else{
		$out= json_encode(["Result" => "failed"]);
    		$response->getBody()->write($out);
	}

	$response = $response->withHeader('Content-Type', 'application/json');
	db_close($db);
    	return $response;
});

$app->get('/login/{user}/{password}', function (Request $request, Response $response) {
	require_once 'function/database.php';
	$db=db_connect();
//	$name = json_encode(["USER"=>$request->getAttribute('user'), "PASSWORD" =>$request->getAttribute('password')] );
 //   	$response->getBody()->write($name);;

	$name= $request->getAttribute('user');	
	$password= sha1($request->getAttribute('password'));	

	if(login_user($name,$password,$db)){
		$token = base64_encode(openssl_random_pseudo_bytes(20)); 
		$result = create_token($name,$token,$db);
		if ($result){
			$out= json_encode(["Result" => "logged", "Token" => $token]);
    			$response->getBody()->write($out);
		}
		else{
			$response->getBody()->write(json_encode(["Result:"=>"failsd","Token"=>"invalid"]));

		}
	}
	else{
		$out= json_encode(["Result" => "login failed"]);
    		$response->getBody()->write($out);
	}


	$response = $response->withHeader('Content-Type', 'application/json');
	db_close($db);
    	return $response;
});


$app->get('/', function (Request $req,  Response $res, $args = []) {
    return $res->withStatus(400)->write('Bad Request');
});

$app->run();

