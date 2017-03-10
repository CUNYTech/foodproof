
<?php

// if not imported import database and user script
  include_once DB;

// ingredient query
function insert_to_ingredient_table($user,$db,&$error){
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO ingredient";
      $sql .= "(name,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . db_escape($db, $user) . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}

function insert_to_user_ingredient($uid,$ingredient_id,$db,&$error){
    
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO `user-ingredient`";
      $sql .= "(user_id,ingredient_id,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . db_escape($db, $uid) . "',";
      $sql .= "'" . db_escape($db, $ingredient_id) . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}

//
  function get_ingredient_name_by_id($ingredient_id,$db,&$error){
      $sql = "SELECT name FROM ingredient ";
      $sql .= "WHERE id='" . $ingredient_id . "' ";
      $sql .= "LIMIT 1;";
            
      $ingredient_result = db_query($db, $sql, $error);
      $ingredient = db_fetch_assoc($ingredient_result);
      
      if (!$ingredient){
      $error["Error"]["Ingredient"]="Ingredient doesnot exist";
      return false;
      }
      return $ingredient['name'];
  }

//
  function get_ingredient_id_by_user_id($uid,$num,$db,&$error){
    $sql = "SELECT `ingredient_id` FROM `user-ingredient` ";
    $sql .= "WHERE `user_id`=" .$uid;
    $sql .= " LIMIT ". $num ." ;";
    $users_result = db_query($db, $sql, $error);
    
    return $users_result;
}
 

?>