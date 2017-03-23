<?php
$app->post('/upload', function($request, $response, $path = null) {
	// if image is sent upload and store image file name
    $error=[];
    $out=[];
    
    $data = $request->getParsedBody();
    if(!isset($data['ingredient'])){$error["Error"]["ingredient"]="not entered";}
   
    if(sizeof($error)==0){
        if(isset($_FILES['image'])) {
            $image_data=image_upload('image',$error);

            if($image_data){
               
                $db=db_connect($error);
                if($db){
                    // upload file
                    $ingredient = filter_var($data['ingredient'],FILTER_SANITIZE_STRING);
                    $ingredient_id=get_ingredient_id_by_name($ingredient,$db,$error);
                    
                    if(sizeof($error)==0){
                        add_image_data($image_data,$ingredient_id,$db,$out);
                        if(sizeof($out)==0){
                            $out['Upload']='succeed';
                        }
                        else{
                            $out['Upload']='failed';
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