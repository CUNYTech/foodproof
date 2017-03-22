<?php
/****************************************************************************
				/ingredient/return
//////////////////////////////////////////////////////////////////////////////

function reads post request on /ingredient/return path 
then it looks for parameters we need
if we dont find them then it throws error
if we do then  creates connection in 
if connection succeeds then it imports query 
it checks if user exists, 
then extracts user id from user table
then extracts content in user-ingredient table with that user id of ampunt directed in post
then it retrieves name of each ingredient id we get
*****************************************************************************
input: POST "user","count" required on /ingredient/return
output: Content-Type', 'application/json file
		if succeeds then returns json string HTTP status 200
			{"result": "succeed", "ingredients":list of ingredient names}
		is fails then HTTP status 403
			{"Error":{the list of error}, "Result": "failed"}
///////////////////////////////////////////////////////////////////////////////
******************************************************************************/

$app->post('/ingredient/return', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['count'])){$error["Count"]["count"]="not entered";}
	
	// if no error continue
	if(sizeof($error)==0){

		$db=db_connect($error);

		if($db){
			// sanitize
			$user= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$num = filter_var($data['count'],FILTER_SANITIZE_NUMBER_INT);
			
			// add user
			$list['ingredients'] = user_ingredient_list($user,$num,$db,$error);
			
			// if no error respond
			if(sizeof($error)==0){
				$list["Result"] = "succeed";
                $out = json_encode($list,JSON_PRETTY_PRINT);
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
