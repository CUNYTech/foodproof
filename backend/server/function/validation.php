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

}

function validate_recipe($recipe,&$error){
	
}

function validate_date($date,&$error){
	$date = trim($date);
	if(strlen($date)!=19 || !DateTime::createFromFormat('Y-m-d H:i:s', $date)) {
		$error['error']['validation']['date']= 'Invalid format incorrect: required "Y-m-d H:i:s"';
	}
}

