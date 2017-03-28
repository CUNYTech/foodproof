<?php
declare(strict_types=1);

function insert_to_user_ingredient(int $uid,int $ingredient_id,$db,array &$error): bool{
    
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO `user-ingredient`";
      $sql .= "(user_id,ingredient_id,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . $uid . "',";
      $sql .= "'" . $ingredient_id . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}

function delete_from_user_ingredient(int $uid, int $iid, $db, array &$error): bool{
	$sql = "DELETE FROM `user-ingredient`";
	$sql .= " WHERE ";
	$sql .= "(`user_id` =". $uid . ") and ";
	$sql .= "(`ingredient_id` =". $iid . ");";

	$result = db_query($db, $sql, $error);
	if(!$result){return false;}
      else{return true;}

}

?>