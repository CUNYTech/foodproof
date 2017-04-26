<?php
declare(strict_types=1);

function validate_uid($uid,&$error){
	if(is_int($uid)){
		if($uid>0){
			return true;
		}
		else{
			$error['error']['validation']['user_id']="user id needs to be positive integer";	
		}
	}
	else{
		$error['error']['validation']['user_id']="user id needs to be integer";
	}
		return false;

}

function validate_recipe($recipe,&$error){
	
}

function validate_date($long_date,&$error){
	if(((string) (int) $long_date == $long_date)){ // type and value compare
		if($long_date>0){
			return true;
		}
		else{
			$error['error']['validation']['date']="date needs to be positive integer";	
			}
		}
		else{
			$error['error']['validation']['date']=$long_date . " date needs to be integer";
		}

		return false;
}

function validate_private($private, &$error){
	if($private=='true' || $private == 'false'){
		return true;
	}
	 else {
		$error['error']['private']="private is flag, needs true or false";
		return false;
	}
}