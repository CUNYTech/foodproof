<?php
declare(strict_types=1);

// if not imported import database and user script
  include_once DB;

function insert_to_recipe_table(int $uid, string $recipe, string $date, $db,array &$error): bool{
	 validate_uid($uid,$error);
	 validate_recipe($recipe,$error);
	 validate_date($date,$error);

	 if(sizeof($error)==0){
	 	$sql = "INSERT INTO recipe";
		$sql .= "(user_id, recipe , created_at) ";
		$sql .= "VALUES (";
		$sql .= "'" . $uid . "',";
		$sql .= "'" . db_escape($db, $recipe) . "',";
		$sql .= "'" . $date . "'";
		$sql .= ");";

		// For INSERT statements, $result is just true/false
		$result = db_query($db, $sql, $error);

		if(!$result){return false;}
		else{return true;}
	 }
	 return false;  
}

function add_recipe($username, $recipe,$date, $db, &$error){
	$uid = get_user_id_by_name($username, $db, $error);
    if(sizeof($error)>0) return Null;

    return insert_to_recipe_table($uid, $recipe, $date, $db, $error);
}

  function get_recipe_list_name_by_uid(int $uid,$db,array &$error): array{
  	$sql ="SELECT * FROM `recipe` WHERE ";
  	$sql.= "`user_id` ='" . $uid . "'";

  	$result = db_query($db, $sql, $error);
    if(sizeof($error)>0) return [];

    $out=[];
    while($row=db_fetch_assoc($result)){
    	$out[]=$row;
    }

    return $out;
  }

//returns date=>recipe
  function get_recipe_and_date_by_user_name(string $username,$db,array &$error){
     $uid = get_user_id_by_name($username, $db, $error);
     if(sizeof($error)>0) return Null;

     $recipe = get_recipe_list_name_by_uid($uid, $db, $error);
     if(sizeof($error)>0) return Null;

     $recipe_out =[];
     foreach ($recipe as $value) {
     	$recipe_out[$value['created_at']][] = $value['recipe'];
     }
     return $recipe_out;
  }

?>