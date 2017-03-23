
<?php

// image query
function insert_to_images_table(array $data,$db,array &$error): bool{
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO images";
      $sql .= "(name,extension,size,md5,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . db_escape($db, $data['name']) . "',";
      $sql .= "'" . db_escape($db, $data['extension']) . "',";
      $sql .= "'" . $data['size'] . "',";
      $sql .= "'" . db_escape($db, $data['md5']) . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}

function insert_to_ingredient_images_table(int $ingredient_id, int $image_id,$db,array &$error): bool{
     $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO `ingredient-images`";
      $sql .= "(image_id,ingredient_id,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . $image_id . "',";
      $sql .= "'" . $ingredient_id . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}
}