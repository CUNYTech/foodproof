<?php
$app->post('/ingredient/delete', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['ingredient'])){$error["Error"]["ingredient"]="not entered";}
    // if no error continue
	if(sizeof($error)==0){

		$db=db_connect($error);

		if($db){

			// sanitize
			$user= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$ingredient = filter_var($data['ingredient'],FILTER_SANITIZE_STRING);

 			// get uid
	      $user_id=get_user_id_by_name($user,$db,$error);
	      if(sizeof($error)==0) {
		      $ingredient_id=get_ingredient_id_by_name($ingredient,$db,$error);
		      if(sizeof($error)==0) {
		      	delete_from_user_ingredient($user_id,$ingredient_id,$db,$error);	
		      	if(sizeof($error)==0) {
		      		$out= json_encode(["Result"=>"succeed"]);
					$response->getBody()->write($out);
		      	}
		      }
	     	}
	     }
			// close connection
			db_close($db);
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