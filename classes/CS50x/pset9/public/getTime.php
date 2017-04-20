
<?php 

    /** Breaks database down based on every day and focuses on the time_of_stop category
     *  fills an array with time_of_stop for each row in the database 
     *  concerns itself with second two parts of the time_of_stop (i.e., hour, minute) --> second is always 00
     *  uses the same day (0,0,0) in order to overlap each data point on the same 24 hour clock
     *  maps each time's values into google charts javascript literal notation 
    */

    // configuration
    require("../includes/config2.php");
 
    $stop_time = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=time_of_stop,count(time_of_stop)&$group=time_of_stop'); //get the html returned from the following url
    $stop_time_row = json_decode($stop_time, true);
    $total_rows = count($stop_time_row);
    $row_num = 0;

    echo("{\"cols\":[{\"label\":\"Date\",\"type\":\"datetime\"},{\"label\":\"Incident Count\",\"type\":\"number\"}],\"rows\":[");
    
    for ($i = 0; $i < $total_rows; $i++) {
        $row_num++;
        $time = $stop_time_row[$i]["time_of_stop"];
        $substring = explode(':', $time);
        $subsubstring_1 = $substring[0];
        $subsubstring_2 = $substring[1];
        $value1 = "Date(0,0,0," . $subsubstring_1 . "," . $subsubstring_2 . ")";
        $value2 = ($stop_time_row[$i]["count_time_of_stop"]);

        if ($row_num == $total_rows)
            {
            echo("{\"c\":[{\"v\":\"$value1\"},{\"v\":$value2}]}");
            }
        else 
            {   
            echo("{\"c\":[{\"v\":\"$value1\"},{\"v\":$value2}]},");
            }
    }
    echo("]}");
    
    /**
    ==> notes on what the javascript liternal should look like (adapted from .json file)
    echo("{\"cols\":[{\"label\":\"Date\",\"type\":\"datetime\"},{\"label\":\"Indoor\",\"type\":\"number\"}],\"rows\":[");
    echo("{\"c\":[{\"v\":\"$place_9\"},{\"v\":1}]},");
    echo("{\"c\":[{\"v\":\"$place_12\"},{\"v\":5}]}");
    echo("]}");
    */

    
?>