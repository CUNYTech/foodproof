<?php
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
			require_once DB;
        	require_once USER;
			
			$db=db_connect($error);

			if($db){
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
