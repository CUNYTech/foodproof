<?php
declare(strict_types=1);

// if not imported import database and user script
  include_once DB;

function insert_to_recipe_table(int $uid, string $recipe, int $date, string $private, $db,array &$error): bool{
	 validate_uid		($uid,$error);
	 validate_recipe	($recipe,$error);
	 validate_date		($date,$error);
	 validate_private	($private,$error);

	 $date_str = date('Y-m-d H:i:s', $date);
	 $val = 0; // default
	 if($val=='true') $private=1;

	 if(sizeof($error)==0){
	 	$sql = "INSERT INTO recipe";
		$sql .= "(user_id, recipe , created_at, private) ";
		$sql .= "VALUES (";
		$sql .= "'" . $uid . "',";
		$sql .= "'" . db_escape($db, $recipe) . "',";
		$sql .= "'" . $date_str . "',";
		$sql .= "'" . $val . "'";
		$sql .= ");";

		// For INSERT statements, $result is just true/false
		$result = db_query($db, $sql, $error);

		if(!$result){return false;}
		else{return true;}
	 }
	 return false;  
}

function add_recipe($username, $recipe, $date, $private, $db, &$error){
	$uid = get_user_id_by_name($username, $db, $error);
    if(sizeof($error)>0) return Null;
    return insert_to_recipe_table($uid, $recipe, $date, $private, $db, $error);
}

  function get_recipe_list_name_by_uid(int $uid,$db,array &$error){
	 validate_uid($uid,$error);
	 if(sizeof($error)==0){
		$sql ="SELECT * FROM `recipe` WHERE ";
		$sql.= "`user_id` ='" . $uid . "'";

		$result = db_query($db, $sql, $error);
		if(sizeof($error)>0) return [];

		return $result;
	 }
  	else{
  		return Null;
  	}
  }

//returns date=>recipe
  function get_recipe_and_date_by_user_name(string $username,$db,array &$error){
     $uid = get_user_id_by_name($username, $db, $error);
     if(sizeof($error)>0) return Null;

     $recipe_result = get_recipe_list_name_by_uid($uid, $db, $error);
     if(sizeof($error)>0) return Null;

    $recipe_out =[];
    while($value = db_fetch_assoc($recipe_result)) {
    	$time = new DateTime($value['created_at']);
     	$recipe_out[]=[$time->getTimestamp(), $value['recipe']];
     }
     return $recipe_out;
  }

  function get_all_recipe_and_date_by_user_name($db, &$error){
  	$sql='SELECT users.name, users.phone, recipe.created_at, recipe.recipe, recipe.private FROM recipe join users where recipe.user_id=users.id order by users.name';

  	$result = db_query($db, $sql, $error);
	if(sizeof($error)>0) return [];

	$recipe_out=[];
	while($value=db_fetch_assoc($result)){
		if((int)$value['private']==0){
			$time = new DateTime($value['created_at']);
			$recipe_out[]=['user'=>$value['name'], 'meals'=>[$time->getTimestamp(), $value['recipe'], $value['phone']]];
		}
	}
	return $recipe_out;
  }

?>