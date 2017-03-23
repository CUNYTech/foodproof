<?php
declare(strict_types=1);

// import db if not here
  include_once DB;
  // user query
  function add_user($name, $password, $email,$db, &$error){
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO users";
      $sql .= "(name,email,password,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . db_escape($db, $name) . "',";
      $sql .= "'" . db_escape($db, $email). "',";
      $sql .= "'" . db_escape($db, $password) . "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";

      // For INSERT statements, $result is just true/false
      $result = db_query($db, $sql, $error);
      
      if(!$result){return false;}
      else{return true;}

  }

 function login_user($name,$password,$db,&$error){
    $sql = "SELECT name FROM users ";
    $sql .= "WHERE (name='" . db_escape($db,$name) . "')";
    $sql .=" AND (password='" . db_escape($db,$password) . "')";
    $sql .= "LIMIT 1;";
      
    $users_result = db_query($db, $sql, $error);
    $user = db_fetch_assoc($users_result);
    
    if (!$user){
     $error["Error"]["User"]="username and password combination doesnot work";
     return false;
    }
    return true;
}

 function create_token($user,$token,$db,&$error){
      $created_at = date("Y-m-d H:i:s");
      $sql = "INSERT INTO sessions ";
      $sql .= "(user,token,created_at) ";
      $sql .= "VALUES (";
      $sql .= "'" . db_escape($db, $user) . "',";
      $sql .= "'" . db_escape($db, $token). "',";
      $sql .= "'" . $created_at . "'";
      $sql .= ");";
      $result = db_query($db, $sql,$error);
      return $result;
 }

function get_user_id_by_name(string $user,$db,array &$error): int{
    $sql = "SELECT id FROM users ";
    $sql .= "WHERE name='" . db_escape($db,$user) . "' ";
    $sql .= "LIMIT 1;";
      
    $users_result = db_query($db, $sql, $error);
    if(sizeof($error)!=0) return -1;

    $user = db_fetch_assoc($users_result);
    
    if (!$user){
     $error["Error"]["User"]="username doesnot exist";
     return -1;
    }
    return (int)($user['id']);
}

function get_user_name_by_id(int $id,$db,array &$error): string{
    $sql = "SELECT name FROM users ";
    $sql .= "WHERE id='" . $id . "' ";
    $sql .= "LIMIT 1;";
      
    $users_result = db_query($db, $sql, $error);
    $user = db_fetch_assoc($users_result);
    
    if (!$user){
     $error["Error"]["User"]="username doesnot exist";
     return "";
    }
    return $user['name'];
}