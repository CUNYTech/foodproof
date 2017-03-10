<?php

function error_to_json($error){
    $out['error']['message']=$error->getMessage();
    $out['error']['file']=$error->getFile();
    $out['error']['line']=$error->getLine();
    $out['error']['trace']=$error->getTrace();

    return json_encode($out,JSON_FORCE_OBJECT | JSON_PRETTY_PRINT);
}


$c['errorHandler'] = function ($c) {
    return function ($request, $response, $exception) use ($c) {
        $err = error_to_json($exception);
        return $c['response']->withStatus(500)
                             ->withHeader('Content-Type', 'application/json')
                             ->write($err);
    };
};

$c['phpErrorHandler'] = function ($c) {
    return function ($request, $response, $exception) use ($c) {
        $err = error_to_json($exception);
        return $c['response']->withStatus(500)
                             ->withHeader('Content-Type', 'application/json')
                             ->write($err);
    };
};

 $c['settings']= [
        'displayErrorDetails' => false, 'debug'=>true,
        'addContentLengthHeader' => false,];

        
return $c;