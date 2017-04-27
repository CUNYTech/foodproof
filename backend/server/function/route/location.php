<?php
$app->post('/location', function ($request, $response,$args) {
	// this is to read post value
	$data = $request->getParsedBody();

	// create error array
	$error=[];

	//check if exists
	//read names from post
	$user 	= filter_var($data['user'],FILTER_SANITIZE_STRING);	
	$lat 	= filter_var($data['lat'],FILTER_SANITIZE_NUMBER_FLOAT,FILTER_FLAG_ALLOW_FRACTION );
	$lon 	= filter_var($data['lon'],FILTER_SANITIZE_NUMBER_FLOAT,FILTER_FLAG_ALLOW_FRACTION );

	if(!$user)	{$error["Error"]["username"]="not entered";}
	if(!$lat)	{$error["Error"]["lat"]="number greater than 0 not entered";}
	if(!$lon)	{$error["Error"]["lon"]="number greater than 0 not entered";}

	
	// check if error happened
	if (sizeof($error)==0){
		$db=db_connect($error);

		if($db){
			// login
			update_location($user,$lat,$lon,$db,$error);

			// if no error, respond
			if (sizeof($error)==0){
					$out= json_encode(["Result" => "succeed"],JSON_FORCE_OBJECT);
					$response->getBody()->write($out);
			}
		
			// close connection
			db_close($db);
		}
	
	}
	
	// check if error happened
	if (sizeof($error)>0){
		$error["Result"]="failed";
		$out= json_encode($error,JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
		$response->getBody()->write($out);
		$response= $response->withStatus(403);
	}
	
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});
