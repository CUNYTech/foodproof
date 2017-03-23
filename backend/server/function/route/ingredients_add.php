<?php
/****************************************************************************
				/ingredient/add
//////////////////////////////////////////////////////////////////////////////

function reads post request on /ingredient/add path 
then it looks for parameters we need
if we dont find them then it throws error
if we do then  creates connection in 
if connection succeeds then it imports query 

*****************************************************************************************
it checks if username exists by checking if user id for that user exists , 
if user_id is returned then add the ingredient to ingredient table
then extract id of that ingredient from ingredient table
add user_id and ingredient_if to the user-ingredient table

*****************************************************************************
input: POST "user","count" required on /ingredient/return
output: Content-Type', 'application/json file
		if succeeds then returns json string HTTP status 200
			{"result": "succeed", "ingredients":list of ingredient names}
		is fails then HTTP status 403
			{"Error":{the list of error}, "Result": "failed"}
///////////////////////////////////////////////////////////////////////////////
******************************************************************************/
$app->post('/ingredient/add', function($request, $response, $path = null) {
	$data = $request->getParsedBody();

	// create error array
	$error=[];
	$out=[];

	//check if exists
	if(!isset($data['user'])){$error["Error"]["username"]="not entered";}
	if(!isset($data['ingredient'])){$error["Error"]["ingredient"]="not entered";}
	
	// if no error continue
	if(sizeof($error)==0){

		// init first
		$db=db_connect($error);

		if($db){
			// sanitize
			$user= filter_var($data['user'],FILTER_SANITIZE_STRING);
			$ingredient = filter_var($data['ingredient'],FILTER_SANITIZE_STRING);
			
			// add user
			$ingredient_id = add_ingredient($user,$ingredient,$db,$error);

			// if image is sent upload and store image file name
			if(isset($_FILES['image'])) {

				// upload file
				$image_data=image_upload('image',$error);

				// save records to db
				if($image_data){
					// no error in this one, continue just display if image not loaded
					add_image_data($image_data,$ingredient_id,$db,$out);
					if(sizeof($out)==0){
						$out['Upload']='succeed';
					}
				}
				else{
					$out['Upload']='failed';
				}
			}
			else {
				$out['Upload']='No file submitted';
			}
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