<?php
declare(strict_types=1);

function validate_uid($uid,&$error){

}

function validate_recipe($recipe,&$error){

}

function validate_date($date,&$error){
	if (!DateTime::createFromFormat('Y-m-d H:i:s', $date)){
		$error['error']['date']=$date. ' not valid format incorrect: needs "Y-m-d H:i:s", provide 0 for seconds if not found';
	}
}

