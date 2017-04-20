
<?php 
    /** Breaks database down per month
     *  fills an array with date_of_stop and number of stops on that day
     *  concerns itself with only the first two parts of the date_of_stop (i.e., the month)
     *  maps the month's values into google charts javascript literal notation 
    */
    
    // configuration
    require("../includes/config2.php");
    $per_year = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=date_of_stop,count(date_of_stop)&$group=date_of_stop');
    $per_year_row = json_decode($per_year, true);
    $total_rows = count($per_year_row);
    $row_num = 0;
    
    //this addition allows you to see time of stop in the chart's visualization 
    //get the html returned from the following url
    $stop_time = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=time_of_stop,count(time_of_stop)&$group=time_of_stop'); 
    $stop_time_row = json_decode($stop_time, true);
    
    echo("{\"cols\":[{\"label\":\"Date\",\"type\":\"datetime\"},{\"label\":\"Incidents per Month\",\"type\":\"number\"}],\"rows\":[");

    for ($i = 0; $i < $total_rows; $i++) {
        
        //this addition allows you to see the time of the stop for violation per month
        $time = $stop_time_row[$i]["time_of_stop"];
        $substring = explode(':', $time);
        $subsubstring_time_1 = $substring[0];
        $subsubstring_time_2 = $substring[1];
        
        $value_time_1 = $subsubstring_time_1 . "," . $subsubstring_time_2;
        $row_num++;
        $year = $per_year_row[$i]["date_of_stop"];
        $keywords = preg_split("/[-T]+/", $year);
        $subsubstring_1 = $keywords[0];
        $subsubstring_2 = $keywords[1];
        $value1 = "Date(" . $subsubstring_1 . "," . $subsubstring_2 . ",0," . $value_time_1 . ")";
        $value2 = ($per_year_row[$i]["count_date_of_stop"]);

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
?>
