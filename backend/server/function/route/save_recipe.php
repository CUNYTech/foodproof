<?php

$app->post('/save_recipe', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];
	$out=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['recipe'])){$error["Error"]["recipe"]="not entered";}
	
	// if no error continue
	if(sizeof($error)==0){

		// init first
		$db=db_connect($error);

		if($db){
			// sanitize
			$user= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$recipe = filter_var($data['recipe'],FILTER_SANITIZE_STRING);
			
			add_recipe($user,$recipe,$db,$error);

			// if no error respond
			if(sizeof($error)==0){
				$out['Result']='succeed';
				$out= json_encode($out,JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
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