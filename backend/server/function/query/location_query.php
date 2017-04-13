<?php
function update_location($user,$lat,$lon,$db,&$error){
	$id = get_user_id_by_name($user, $db, $error);
	if(sizeof($error)==0){
		if(location_exists($id,$db,$error)){
			if(sizeof($error)==0){
				update_location_data($id,$lat,$lon,$db,$error);
			}
			
		}
		else{
						if(sizeof($error)==0){

			insert_location_data($id,$lat,$lon,$db,$error);
		}
		}
	}
}
function location_exists($id,$db,&$error){
	 $sql = "SELECT * FROM user_location ";
    $sql .= "WHERE user_id='" . $id . "' ";
    $sql .= "LIMIT 1;";
    $users_result = db_query($db, $sql, $error);
    if(sizeof($error)==0){
	    $user = db_fetch_assoc($users_result);
	    
	    if (!$user){
	     	return false;
	    }
	    else return true;
	}
}

function update_location_data($user,$lat,$lon,$db,&$error){
	  $created_at = date("Y-m-d H:i:s");
      $sql = "UPDATE `user_location` SET ";
      $sql.= "`user_id`= '"	.$user 	."' ,";
      $sql.= "`lat`= '"		.$lat	."' ,";
      $sql.= "`lon`= '"		.$lon	."',";
      $sql.="`created_at`= '"	.$created_at. "'";
      $sql.= " WHERE 1";
      $result = db_query($db, $sql,$error);
      return $result;
}

function insert_location_data($user,$lat,$lon,$db,&$error){
	$created_at = date("Y-m-d H:i:s");
	$sql = "INSERT INTO user_location ";
	$sql .= "(user_id,lat,lon,created_at) ";
	$sql .= "VALUES (";
	$sql .= "'" . $user . "',";
	$sql .= "'" . $lat. "',";
	$sql .= "'" . $lon. "',";
	$sql .= "'" . $created_at . "'";
	$sql .= ");";
	$result = db_query($db, $sql,$error);
	return $result;
}
