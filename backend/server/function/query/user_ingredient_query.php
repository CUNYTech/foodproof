<?php
declare(strict_types=1);

function insert_to_user_ingredient(int $uid,int $ingredient_id,$db,array &$error): bool{
    
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO `user_ingredient`";
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
	$sql = "DELETE FROM `user_ingredient`";
	$sql .= " WHERE ";
	$sql .= "(`user_id` =". $uid . ") and ";
	$sql .= "(`ingredient_id` =". $iid . ");";

	$result = db_query($db, $sql, $error);
	if(!$result){return false;}
      else{return true;}

}


 function user_ingredient_list(string $user,int $num,$db,&$error): array{
      $sql = "SELECT `ingredient`.name from `users` join `user_ingredient` on `users`.id=`user_ingredient`.user_id join ingredient on `user_ingredient`.ingredient_id=`ingredient`.id ";
      $sql .= "where `users`.name='" . $user ."'";

      $ingredient_list = [];
      $result = db_query($db, $sql, $error);

      if(sizeof($error)==0){
            while($ingredient = db_fetch_assoc($result)){
                  $ingredient_list[]=$ingredient['name'];
            }

            return $ingredient_list;
      }
      else{
            return null;
      }
  }

?>