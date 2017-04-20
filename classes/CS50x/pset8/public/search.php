
<?php

    require(__DIR__ . "/../includes/config.php");                                               //geo is city        state        postal code
    
    
    // numerically indexed array of places
    $places = [];                                                                               //creating array
    
    $places = NULL;
    // TODO: search database for places matching $_GET["geo"], store in $places
    
    $places = CS50::query("SELECT * FROM places WHERE postal_code = ?", $_GET["geo"]);
    if ($places == NULL)                                                                        // || sizeof($places) < 100
        {
        $places = CS50::query("SELECT * FROM places WHERE admin_code1 LIKE ?", '%' . $_GET["geo"]);
        if ($places == NULL || sizeof($places) < 5)
            {
            $places = CS50::query("SELECT * FROM places WHERE place_name = ?", '%' . $_GET["geo"] . '%');
            if ($places == NULL)
                {
                $places = CS50::query("SELECT * FROM places WHERE place_name LIKE ?", '%' . $_GET["geo"] . '%');
                if ($places == NULL)                                                                                                //someone put a comma between city,state 
                    {
                    $places = CS50::query("SELECT * FROM places WHERE MATCH(place_name, admin_name1) AGAINST (?)", $_GET["geo"]);
                    }
                }
            }
        }
    // output places as JSON (pretty-printed for debugging convenience)
    header("Content-type: application/json");
    print(json_encode($places, JSON_PRETTY_PRINT));                                             //pretty prints that array

?>