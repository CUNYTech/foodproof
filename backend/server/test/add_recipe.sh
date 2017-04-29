
function save_recipe(){
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
	curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad salad" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 1" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 2" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 3" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 4" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 5" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 6" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F  "date=$DATE" -F "recipe=potato salad 7" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 8" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 9" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 0" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 11" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 34" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 12" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 234" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 23" $*/save_recipe;
    DATE=`date +%Y-%m-%d\ %H:%M:%S`
    curl -X POST -F "user=binod" -F "date=$DATE" -F "recipe=potato salad 23432" $*/save_recipe;
}

function get_recipe(){
    curl -X POST -F "user=binod"  $*/get_recipe;
    curl -X POST -F "user=binasod"  $*/get_recipe;

}


