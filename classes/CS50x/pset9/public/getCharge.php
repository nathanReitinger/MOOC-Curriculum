<?php 

    /** Breaks database down into two categories: (1) charging statute; and (2) count
     *  the statute is the instrument used to penalize the offender (i.e., 21-801.1 is the speeding statute)
     *  fills an array with charge identifier and total rows with that charge included
     *  maps each day's values into google charts javascript literal notation 
    */
        
    // configuration
    require("../includes/config2.php");

    echo ("{ \"cols\": [ {\"id\":\"\",\"label\":\"Charge\",\"pattern\":\"\",\"type\":\"string\"}, {\"id\":\"\",\"label\":\"Interactions\",\"pattern\":\"\",\"type\":\"number\"} ], \"rows\": [ ");

    $charge = file_get_contents('https://data.montgomerycountymd.gov/resource/ms8i-8ux3.json?$select=charge,count(charge)&$group=charge');
    $charge_row = json_decode($charge, true);
    $total_rows = count($charge_row);
    $row_num = 0;
    
    for ($i = 0; $i < $total_rows; $i++)
        {
        if ($charge_row[$i]['count_charge'] > 20000) 
            {
            $row_num++;
            $value1 = $charge_row[$i]["charge"];
            $value2 = ($charge_row[$i]["count_charge"]);
            
            if ($row_num == $total_rows)
                  {
                  echo("{\"c\":[{\"v\":\"" . $value1 . "\",\"f\":null},{\"v\":" . $value2 . ",\"f\":null}]}");
                  } 
            else 
                  {
                  echo("{\"c\":[{\"v\":\"" . $value1 . "\",\"f\":null},{\"v\":" . $value2 . ",\"f\":null}]}, ");
                  }
            }
        }
  echo(" ] }");

?>