<?php

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

        require_once USER;

		$db=db_connect($error);

		if($db){
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
