<?php
declare(strict_types=1);

function image_upload(string $name, string $folder, array &$error){
    $storage = new \Upload\Storage\FileSystem($folder);
    $file = new \Upload\File($name,$storage);

    //rename the file on upload
    $new_filename = uniqid();
    $file->setName($new_filename);

    // Validate file upload
    // MimeType List => http://www.iana.org/assignments/media-types/media-types.xhtml
    $file->addValidations(array(
        // Ensure file is of type "image/png , bmp , jpg ..
        new \Upload\Validation\Mimetype(array('image/png', 'image/gif','image/jpeg','image/bmp')),

        // Ensure file is no larger than 5M (use "B", "K", M", or "G")
        new \Upload\Validation\Size('5M')
    ));

    // Access data about the file that has been uploaded
    $data = array(
        'name'       => $file->getNameWithExtension(),
        'extension'  => $file->getExtension(),
        'size'       => $file->getSize(),
        'md5'        => $file->getMd5()
            );

    // Try to upload file
    try {
        // Success!
        $file->upload();
        return $data;
    } catch (\Exception $e) {
        // Fail!
        return NULL;
    }  
}

function add_ingredient_image_data(array $data,int $ingredient_id, $db,&$error){
    if(insert_to_images_table($data,$db,$error)){
        $image_id = db_insert_id($db);
        insert_to_ingredient_images_table($ingredient_id,$image_id,$db,$error);
        //echo json_encode($error);
    }
}

function add_user_profile_picture_data(array $data,int $user_id, $db,&$error){
    if(insert_to_profile_picture_table($data,$db,$error)){
        $image_id = db_insert_id($db);
        insert_to_user_profile_pictures_table($user_id,$image_id,$db,$error);
        //echo json_encode($error);
    }
}