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

/****************************************************************************
			/register
//////////////////////////////////////////////////////////////////////////////

function reads post request on /register path 
then it looks for parameters we need
if we dont find them then it throws error
if we do then it imports database file and creaes connection
if connection succeeds then it imports query file and registers user to db
*****************************************************************************
input: POST "user","email","password" on /register
output: Content-Type', 'application/json file
		if succeeds then returns json string http status 200
			{"result": "succeed"}
		is fails then http status 403
			{"Error":{the list of error}, "Result": "failed"}
///////////////////////////////////////////////////////////////////////////////
******************************************************************************/

$app->post('/register', function ($request, $response,$args) {
	// this is to read post value
	$data = $request->getParsedBody();

	// create error array
	$error=[];
	
	//check if get required 
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['password'])){$error["Error"]["password"]="not entered";}
	if(!isset($data['email'])){$error["Error"]["email"]= "not entered";}
	
	// if no error continue
	if(sizeof($error)==0){
			// connect db
			require_once '../function/database.php';
			$db=db_connect($error);

			if($db){
				require_once '../function/user_query_functions.php';
			// sanitize
			$name= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$password = filter_var($data['password'],FILTER_SANITIZE_STRING);
			$email = filter_var($data['email'],FILTER_SANITIZE_STRING);
			
			// add user
			$add = add_user($name,$password,$email,$db,$error);
			
			// if no error respond
			if(sizeof($error)==0){
				$out= json_encode(["Result" => "succeed"]);
				$response->getBody()->write($out);
			}
			
			// close connection
			db_close($db);
		}
	}
	// check error
	if (sizeof($error)>0){
		$error["Result"]="failed";
		$out= json_encode($error,JSON_FORCE_OBJECT);
		$response->getBody()->write($out);
		$response= $response->withStatus(403);
	}
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});


/****************************************************************************
				/login
//////////////////////////////////////////////////////////////////////////////

function reads post request on /login path 
then it looks for parameters we need
if we dont find them then it throws error
if we do then it imports database file and creaes connection
if connection succeeds then it imports query file and checks if user password combination exists
if it exists then it generates a random token and stores (tokem user) info to db
then it returns token
*****************************************************************************
input: POST "user","password" required on /login
output: Content-Type', 'application/json file
		if succeeds then returns json string HTTP status 200
			{"result": "succeed", "token":token value}
		is fails then HTTP status 403
			{"Error":{the list of error}, "Result": "failed"}
///////////////////////////////////////////////////////////////////////////////
******************************************************************************/


$app->post('/login', function ($request, $response,$args) {
	// this is to read post value
	$data = $request->getParsedBody();

	// create error array
	$error=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['password'])){$error["Error"]["password"]="not entered";}
	
	
	// check if error happened
	if (sizeof($error)==0){
		require_once '../function/database.php';
		$db=db_connect($error);

		if($db){
			require_once '../function/user_query_functions.php';

			//read names from post
			$name= filter_var($data['user'],FILTER_SANITIZE_STRING);	
			$password = filter_var($data['password'],FILTER_SANITIZE_STRING);

			// login
			login_user($name,$password,$db,$error);

			// if no error, respond
			if (sizeof($error)==0){

				// generate token
				$token = base64_encode(openssl_random_pseudo_bytes(20)); 
				create_token($name,$token,$db,$error);

				// if no error continue
				if (sizeof($error)==0){
					$out= json_encode(["Result" => "succeed", "Token" => $token]);
					$response->getBody()->write($out);
				}
			}
			// close connection
			db_close($db);
		}
	
	}
	
	// check if error happened
	if (sizeof($error)>0){
		$error["Result"]="failed";
		$out= json_encode($error,JSON_FORCE_OBJECT);
		$response->getBody()->write($out);
		$response= $response->withStatus(403);
	}
	
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});

/****************************************************************************
				/*
//////////////////////////////////////////////////////////////////////////////

function reads any HTTP request on site we didnot define 
*****************************************************************************
input: any request we did not define
output: Content-Type', 'application/json file with status 400
			{"Result":"failed","Error":{"path":"invalid"}}

///////////////////////////////////////////////////////////////////////////////
******************************************************************************/
$app->any('/[{path:.*}]', function($request, $response, $path = null) {
	$error["Result"]="failed";
	$error["Error"]["path"]="invalid";	
	$out= json_encode($error,JSON_FORCE_OBJECT);
	$response->getBody()->write($out);
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response->withStatus(400);
});

$app->run();

