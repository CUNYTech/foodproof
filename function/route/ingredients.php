<?php
$app->post('/ingredient/add', function($request, $response, $path = null) {
	$test["Result"]="test";
	$out= json_encode($test,JSON_FORCE_OBJECT);
	$response->getBody()->write($out);
	$response = $response->withHeader('Content-Type', 'application/json');
    return $response;
});