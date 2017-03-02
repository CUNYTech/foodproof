<?php
  require_once('db_credentials.php');

  function db_connect(&$error) {
    $connection = mysqli_connect(DB_SERVER, DB_USER, DB_PASS, DB_NAME);
    if(mysqli_connect_errno()) {
      $msg = "Database connection failed: ";
      $msg .= mysqli_connect_error();
      $msg .= " (" . mysqli_connect_errno() . ")";
      $error['connection Error'] = $msg;
    exit;
	}
    return $connection;
  }

  function db_query($connection, $sql, &$error) {
    	$result_set = mysqli_query($connection, $sql);
	if($result_set==false){
		$error['SQL error']=mysqli_error($connection);
		return false;
	}
	else{
		return $result_set;
	}
  }


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
    	$sql .= "WHERE (name='" . db_escape($db,$name) . "') and(password='" . db_escape($db,$password) . "');";
    	$result = db_query($db, $sql,$error);
	if (mysqli_num_rows($result)==1){
		return true;
	}

	return false;
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
	//echo $sql;
    // For INSERT statements, $result is just true/false
    $result = db_query($db, $sql,$error);
    return $result;

	
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

