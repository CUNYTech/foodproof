<?php
$app->post('/profile_picture', function($request, $response, $path = null) {
	// if image is sent upload and store image file name
    $error=[];
    $out=[];
    
    $data = $request->getParsedBody();
    if(!isset($data['user'])){$error["Error"]["User"]="not entered";}
   
    if(sizeof($error)==0){
        if(isset($_FILES['image'])) {
            $image_data=image_upload('image',PROFILE_PICTURE, $error);
            if($image_data){
                $db=db_connect($error);
                if($db){
                    // upload file
                    $user = filter_var($data['user'],FILTER_SANITIZE_STRING);
                    $user_id=get_user_id_by_name($user,$db,$error);
                    
                    if(sizeof($error)==0){
                        add_user_profile_picture_data($image_data,$user_id,$db,$error);
                        if(sizeof($error)==0){
                            $out['Upload']='succeed';
                        }
                        else{
                            $error['Upload']='failed';
                            unlink(PROFILE_PICTURE.'/'.$image_data['name']);
                        }
                    }
                    
                  // close connection
                    db_close($db);
                }

            }
            if(sizeof($error)==0){
                $out['Result']='succeed';
                $out= json_encode($out,JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
                $response->getBody()->write($out);
            }
        }
        else {
            $error['Error']['Upload']='No file submitted';
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