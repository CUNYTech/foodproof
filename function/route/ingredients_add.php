<?php
$app->post('/ingredient/add', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['ingredient'])){$error["Error"]["ingredient"]="not entered";}
	
	// if no error continue
	if(sizeof($error)==0){
        	require_once INGREDIENT;
			require_once USER;

			$db=db_connect($error);

			if($db){
			// sanitize
			$user= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$ingredient = filter_var($data['ingredient'],FILTER_SANITIZE_STRING);
			
			// add user
			add_ingredient($user,$ingredient,$db,$error);
			
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
	$out= json_encode($test,JSON_FORCE_OBJECT);
	$response->getBody()->write($out);
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});