
<?php 

    /** Breaks database down per day
     *  fills an array with date_of_stop and number of stops on that day
     *  concerns itself with the first three parts of the date_of_stop (i.e., year, month, day)
     *  optinally uses optimization to show time_of_stop on hover through google's tooltip 
     *  maps each day's values into google charts javascript literal notation 
    */
    
    // configuration
    require("../includes/config2.php");
 
    $per_year = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=date_of_stop,count(date_of_stop)&$group=date_of_stop');
    $per_year_row = json_decode($per_year, true);
    $total_rows = count($per_year_row);
    $row_num = 0;
    
    //this optimization is used to display correct informational boxes
    $stop_time = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=time_of_stop,count(time_of_stop)&$group=time_of_stop'); //get the html returned from the following url
    $stop_time_row = json_decode($stop_time, true);
    
    echo("{\"cols\":[{\"label\":\"Date\",\"type\":\"datetime\"},{\"label\":\"Total Incident per Day\",\"type\":\"number\"}],\"rows\":[");
    
    for ($i = 0; $i < $total_rows; $i++) {
        
        //optimization allows users to hover over data points and see exact times
        $time = $stop_time_row[$i]["time_of_stop"];
        $substring = explode(':', $time);
        $subsubstring_time_1 = $substring[0];
        $subsubstring_time_2 = $substring[1];
        $value_time_1 = "," . $subsubstring_time_1 . "," . $subsubstring_time_2 . ")";

        $row_num++;
        $year = $per_year_row[$i]["date_of_stop"];
        $keywords = preg_split("/[-T]+/", $year);
        $subsubstring_1 = $keywords[0];
        $subsubstring_2 = $keywords[1];
        $subsubstring_3 = $keywords[2];
        $value1 = "Date(" . $subsubstring_1 . "," . $subsubstring_2 . "," . $subsubstring_3 . $value_time_1;
        $value2 = ($per_year_row[$i]["count_date_of_stop"]);
        
        //print("\n\n" . $value1 . "\n\n");
        
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