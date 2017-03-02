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
$app->post("/test",function($request, $response,$args) {
	$data = $request->getParsedBody();
	$test=[];
	$test['text'] = filter_var($data['text'], FILTER_SANITIZE_STRING);

	$test_out = json_encode($test);
	$response = $response->withHeader('Content-Type', 'application/json');
	$response->getBody()->write($test_out);
	return $response;

});

$app->post('/register', function ($request, $response,$args) {
	require_once '../function/database.php';
	$error=[];
	$error["Result"]= "failed";	
	$db=db_connect($error);

	// this is to read post value
	$data = $request->getParsedBody();

	//read names from post
	$name= filter_var($data['user'],FILTER_SANITIZE_STRING);	
	$password = filter_var($data['password'],FILTER_SANITIZE_STRING);
	$email = filter_var($data['email'],FILTER_SANITIZE_STRING);
	
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
$app->post('/login', function ($request, $response,$args) {
	require_once '../function/database.php';
	$error=[];
	//dedfalult error
	$error["Result"]="failed";
	$db=db_connect($error);

	// this is to read post value
	$data = $request->getParsedBody();

	//read names from post
	$name= filter_var($data['user'],FILTER_SANITIZE_STRING);	
	$password = filter_var($data['password'],FILTER_SANITIZE_STRING);

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
    return $res->withStatus(400)->write('Error in parsing');
});

$app->run();

