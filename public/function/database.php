<?php
  require_once('db_credentials.php');

  function db_connect() {
    $connection = mysqli_connect(DB_SERVER, DB_USER, DB_PASS, DB_NAME);
    if(mysqli_connect_errno()) {
      $msg = "Database connection failed: ";
      $msg .= mysqli_connect_error();
      $msg .= " (" . mysqli_connect_errno() . ")";
      echo $msg;
    exit;
	}
    return $connection;
  }

  function db_query($connection, $sql) {
    $result_set = mysqli_query($connection, $sql);
    return $result_set;
  }


  function add_user($name, $password, $email,$db){

    $sql = "INSERT INTO users";
    $sql .= "(name,email,password) ";
    $sql .= "VALUES (";
    $sql .= "'" . db_escape($db, $name) . "',";
    $sql .= "'" . db_escape($db, $email). "',";
    $sql .= "'" . db_escape($db, $password) . "'";
    $sql .= ");";

    // For INSERT statements, $result is just true/false
    $result = db_query($db, $sql);
    if($result) {
      return true;
    } else {
	return false;
    }


  }

 function login_user($name,$password,$db){
	$sql = "SELECT * FROM users ";
    	$sql .= "WHERE (name='" . db_escape($db,$name) . "') and(password='" . db_escape($db,$password) . "');";
    	$result = db_query($db, $sql);
	//echo $sql;		
	if ($result){
		return true;
	}

	return false;
 }

 function create_token($user,$token,$db){
    $sql = "INSERT INTO sessions ";
    $sql .= "(name,token) ";
    $sql .= "VALUES (";
    $sql .= "'" . db_escape($db, $user) . "',";
    $sql .= "'" . db_escape($db, $token). "'";
    $sql .= ");";
	echo $sql;
    // For INSERT statements, $result is just true/false
    $result = db_query($db, $sql);
    if($result) {
      return true;
    } else {
        return false;
    }

	
 }

  function db_fetch_assoc($result_set) {
    return mysqli_fetch_assoc($result_set);
  }

  function db_free_result($result_set) {
    return mysqli_free_result($result_set);
  }

  function db_num_rows($result_set) {
    return mysqli_num_rows($result_set);
  }

  function db_insert_id($connection) {
    return mysqli_insert_id($connection);
  }

  function db_error($connection) {
    return mysqli_error($connection);
  }

  function db_close($connection) {
    return mysqli_close($connection);
  }

  function db_escape($connection, $string) {
    return mysqli_real_escape_string($connection, $string);
  }

?>

