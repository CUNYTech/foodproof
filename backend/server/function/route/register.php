<?php
/****************************************************************************
			/register
//////////////////////////////////////////////////////////////////////////////

function reads post request on /register path 
then it looks for parameters we need
if we dont find them then it throws error
if we do then  creaes connection in 
if connection succeeds then it 
********************************************************************
 just adds user to db table, 
if there is no error it created random token,
then it adds user_name and token to the sessions db

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
	$name 		= filter_var($data['user'],		FILTER_SANITIZE_STRING);
	$password 	= filter_var($data['password'],	FILTER_SANITIZE_STRING);
	$email 		= filter_var($data['email'],	FILTER_SANITIZE_STRING);
	$phone 		= filter_var($data['phone'],	FILTER_SANITIZE_STRING);

	if(!$name)		{$error["Error"]["username"]	="not entered";}
	if(!$password)	{$error["Error"]["password"]	="not entered";}
	if(!$email)		{$error["Error"]["email"]		= "not entered";}
	if(!$phone)		{$error["Error"]["phone"]		= "not entered";}

	// if no error continue
	if(sizeof($error)==0){

			$db=db_connect($error);

			if($db){
			// sanitize
			$name= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$password = filter_var($data['password'],FILTER_SANITIZE_STRING);
			$email = filter_var($data['email'],FILTER_SANITIZE_STRING);
			$phone = filter_var($data['phone'],FILTER_SANITIZE_STRING);

			// add user
			$add = add_user($name,$password,$email,$phone,$db,$error);
			
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
		$out= json_encode($error,JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
		$response->getBody()->write($out);
		$response= $response->withStatus(403);
	}
	
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});
