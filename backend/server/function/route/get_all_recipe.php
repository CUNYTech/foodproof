<?php

$app->post('/get_all_recipe', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];
	$out=[];
	
	// if no error continue
	if(sizeof($error)==0){

		// init first
		$db=db_connect($error);

		if($db){

			$recipe_list = get_all_recipe_and_date_by_user_name($db,$error);

			// if no error respond
			if(sizeof($error)==0){
				$out['Result']='succeed';
				$out['Recipes'] =$recipe_list;
				$out= json_encode($out, JSON_PRETTY_PRINT);
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