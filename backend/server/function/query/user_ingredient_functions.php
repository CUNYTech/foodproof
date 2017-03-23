<?php
declare(strict_types=1);

include_once INGREDIENT;

function add_ingredient($user,$ingredient,$db, &$error): int{
      // get uid
      $user_id=get_user_id_by_name($user,$db,$error);
      if(sizeof($error)!=0) return -1;
     
      $ingredient_id=0;
      
      // check if ingredient already exists
      $dummy_error=[];
      $ingredient_id=get_ingredient_id_by_name($ingredient,$db,$dummy_error);

      // if ingredient doesnot exist yet add it
      if(sizeof($dummy_error)!=0){
        // insert ingredient to table and get its uid
        insert_to_ingredient_table($ingredient,$db,$error);      
        if(sizeof($error)!=0) return -1;

        // get last autoincrement id
        $ingredient_id =  db_insert_id($db);
      }

      //insert to user-ingretdient tuple table
      insert_to_user_ingredient($user_id,$ingredient_id,$db,$error);      
      if(sizeof($error)!=0) return -1;
      
      return $ingredient_id;
  }


 function user_ingredient_list(string $user,int $num,$db,&$error): array{
      // get uid
      $user_id=get_user_id_by_name($user,$db,$error);
      if(sizeof($error)!=0) return NULL;

      // get list of ingredient uid
      $ingredient_list=[];
      $ingredient_result = get_ingredient_id_by_user_id($user_id,$num,$db,$error);
      if(sizeof($error)!=0) return NULL;

      while($ingredient=db_fetch_assoc($ingredient_result) ){
          $ingredient_list[] = get_ingredient_name_by_id((int)$ingredient['ingredient_id'],$db,$error);
          if(sizeof($error)!=0) return NULL;

      }

      return $ingredient_list;
  }