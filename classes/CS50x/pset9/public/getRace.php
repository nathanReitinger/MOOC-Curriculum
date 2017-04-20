
<?php 


    /** Breaks database down into two categories: (1) race; and (2) count 
     *  fills an array with race identifier and total rows with that race included
     *  maps each day's values into google charts javascript literal notation 
    */

    // configuration
    require("../includes/config2.php");

    //attempt 1: static
    //$rows = CS50::query("SELECT race, COUNT(race) AS `value_occurrence`  FROM info GROUP BY race ORDER BY `value_occurrence`");
    //print_r($rows);
    //$result = mysql_query($sql_query);
    //print ("{ \"cols\": [ {\"id\":\"\",\"label\":\"Name-Label\",\"pattern\":\"\",\"type\":\"string\"}, {\"id\":\"\",\"label\":\"PointSum\",\"pattern\":\"\",\"type\":\"number\"} ], \"rows\": [ ");

    //attempt 2: dynamic
    $race = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=race,count(race)&$group=race');
    $race_row = json_decode($race, true);
  
    echo ("{ \"cols\": [ {\"id\":\"\",\"label\":\"Race\",\"pattern\":\"\",\"type\":\"string\"}, {\"id\":\"\",\"label\":\"Interactions\",\"pattern\":\"\",\"type\":\"number\"} ], \"rows\": [ ");

    $total_rows = count($race_row);
    $row_num = 0;
    for ($i = 0; $i < $total_rows; $i++)
    {
        $row_num++;
        $value1 = $race_row[$i]["race"];
        $value2 = ($race_row[$i]["count_race"]);
    if ($row_num == $total_rows)
          {
          echo("{\"c\":[{\"v\":\"" . $value1 . "\",\"f\":null},{\"v\":" . $value2 . ",\"f\":null}]}");
          } 
      else 
          {
          echo("{\"c\":[{\"v\":\"" . $value1 . "\",\"f\":null},{\"v\":" . $value2 . ",\"f\":null}]}, ");
          }
    }
  echo(" ] }");

?>
