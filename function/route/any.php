<?php
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
