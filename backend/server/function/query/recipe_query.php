<?php
declare(strict_types=1);

// if not imported import database and user script
  include_once DB;

function insert_to_recipe_table(int $uid, string $recipe,$db,array &$error): bool{
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO recipe";
      $sql .= "(user_id, recipe , created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . $uid . "',";
      $sql .= "'" . db_escape($db, $recipe) . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}

function add_recipe($username, $recipe, $db, &$error){
	$uid = get_user_id_by_name($username, $db, $error);
    if(sizeof($error)>0) return Null;

    return insert_to_recipe_table($uid, $recipe, $db, $error);
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
     	$recipe_out[$value['created_at']] = $value['recipe'];
     }
     return $recipe_out;
  }

?>