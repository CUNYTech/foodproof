<?php

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

// manage registration
$app->get('/register/{user}/{password}/{email}', function (Request $request, Response $response) {
	require_once 'function/database.php';
	$error=[];
	$error["Result"]= "failed";
	
	$db=db_connect($error);
	
	//read names from url for noe
	$name= $request->getAttribute('user');	
	$password= sha1($request->getAttribute('password'));	
	$email= $request->getAttribute('email');	

	// add user
	$add = add_user($name,$password,$email,$db,$error);
	
   	if($add){
		$out= json_encode(["Result" => "succeed"]);
    		$response->getBody()->write($out);
	}
	else{
		$out= json_encode($error);
    		$response->getBody()->write($out);
	}

	$response = $response->withHeader('Content-Type', 'application/json');
	db_close($db);
    	return $response;
});

// manage login
$app->get('/login/{user}/{password}', function (Request $request, Response $response) {
	require_once 'function/database.php';
	$error=[];
	//dedfalult error
	$error["Result"]="failed";

	$db=db_connect($error);
	// read rom url for now
	
	$name= $request->getAttribute('user');	
	$password= sha1($request->getAttribute('password'));	
	
	// login
	$log = login_user($name,$password,$db,$error);
	if($log){
		$token = base64_encode(openssl_random_pseudo_bytes(20)); 
		$result = create_token($name,$token,$db,$error);
		if ($result){
			$out= json_encode(["Result" => "succeed", "Token" => $token]);
    			$response->getBody()->write($out);
		}
		else{
			$response->getBody()->write(json_encode($error));

		}
	}
	else{
		$out= json_encode($error);
    		$response->getBody()->write($out);
	}


	$response = $response->withHeader('Content-Type', 'application/json');
	db_close($db);
    	return $response;
});

// do default error
$app->get('/', function (Request $req,  Response $res, $args = []) {
    return $res->withStatus(400)->write('Bad Request');
});

$app->run();

