
<?php

// if not imported import database and user script
  require_once DB;

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

function add_ingredient($user,$ingredient,$db, &$error){
     // get uid
      $user_id=get_user_id_by_name($user,$db,$error);
      if(sizeof($error)!=0) return false;

      // insert ingredient to table and get its uid
      insert_to_ingredient_table($ingredient,$db,$error);      
      if(sizeof($error)!=0) return false;

      // get last autoincrement id
      $ingredient_id =  db_insert_id($db);

      //insert to user-ingretdient tuple table
      insert_to_user_ingredient($user_id,$ingredient_id,$db,$error);      
      if(sizeof($error)!=0) return false;
  }

?>