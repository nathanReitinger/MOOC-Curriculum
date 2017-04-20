<?php 

    /** Breaks database down into two categories: (1) gender; and (2) count 
     *  fills an array with gender identifier and total rows with that gender included
     *  maps each day's values into google charts javascript literal notation 
    */
    
    // configuration
    require("../includes/config2.php");
        
    //hardcoded first try (using phpmyadmin database) 
    //$total = CS50::query("SELECT gender, COUNT(gender) AS `value_occurrence`  FROM info GROUP BY gender ORDER BY `value_occurrence`");
    //$male_total = ($total[2]["value_occurrence"]) + 1;
    //$female_total = ($total[1]["value_occurrence"]) + 1;
    //$unidentified_total = ($total[0]["value_occurrence"]) + 1;        //too small to count, purposfully not included

    //live coded second try
    $gender = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=gender,count(gender)&$group=gender');
    $sub_array = json_decode($gender, true);
    $male_total = ($sub_array[0]["count_gender"]);
    $female_total = $sub_array[1]["count_gender"];
    
    $female = "Female";
    $male = "Male";
    
    //returns format appropriate for google charts 
    echo ("{ \"cols\": [ {\"id\":\"\",\"label\":\"Gender\",\"pattern\":\"\",\"type\":\"string\"}, {\"id\":\"\",\"label\":\"Interactions\",\"pattern\":\"\",\"type\":\"number\"} ], \"rows\": [ ");
    echo("{\"c\":[{\"v\":\"" . $male . "\",\"f\":null},{\"v\":" . $male_total . ",\"f\":null}]} ,");
    echo("{\"c\":[{\"v\":\"" . $female . "\",\"f\":null},{\"v\":" . $female_total . ",\"f\":null}]}");
    echo(" ] }");

?>