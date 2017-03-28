<?php
declare(strict_types=1);

// if not imported import database and user script
  include_once DB;

// ingredient query
function insert_to_ingredient_table(string $user,$db,array &$error): bool{
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

  function get_ingredient_name_by_id(int $ingredient_id,$db,array &$error): string{
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
  function get_ingredient_id_by_name(string $ingredient,$db,array &$error){
      $sql = "SELECT id FROM ingredient ";
      $sql .= "WHERE name='" . db_escape($db,$ingredient) . "' ";
      $sql .= "LIMIT 1;";
            
      $ingredient_result = db_query($db, $sql, $error);
      $ingredient = db_fetch_assoc($ingredient_result);
      
      if (!$ingredient){
      $error["Error"]["Ingredient"]="Ingredient doesnot exist";
      return false;
      }
      return (int)$ingredient['id'];
  }

//
  function get_ingredient_id_by_user_id(int $uid,int $num,$db,array &$error){
    $sql = "SELECT `ingredient_id` FROM `user-ingredient` ";
    $sql .= "WHERE `user_id`=" .$uid;
    $sql .= " LIMIT ". $num ." ;";
    $users_result = db_query($db, $sql, $error);
    
    return $users_result;
}
 

?>