<?php
declare(strict_types=1);

  include_once('db_credentials.php');

  function db_connect(array &$error) {
      $connection = mysqli_connect(DB_SERVER, DB_USER, DB_PASS, DB_NAME);
      if(mysqli_connect_errno()) {
        $msg = "Database connection failed: ";
        $msg .= mysqli_connect_error();
        $msg .= " (" . mysqli_connect_errno() . ")";
        $error['connection Error'] = $msg;
        return false;
    }
      return $connection;
  }

  function db_query($connection, string $sql,array &$error) {
    	$result_set = mysqli_query($connection, $sql);
      if($result_set==false){
        $error['SQL error']=mysqli_error($connection);
        return false;
      }
      else{
        if(substr($sql, 0, 7) == 'SELECT ') {
            confirm_query($result_set,$error);
        }
      }
      return $result_set;
  }

function confirm_query($result_set,array &$error) {
    if(!$result_set) {
      $error["error"]["DB"]="Database query failed";
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

