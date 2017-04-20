<?php

    // configuration
    require("../includes/config.php");

        $rows = CS50::query("SELECT symbol, shares FROM portfolio WHERE user_id = ?", $_SESSION["id"]);                             //provided by CS50 selecting my rows for the provided chart
        
        
        
        $positions = [];                                                                                                            //to fill the new array
        foreach ($rows as $row)
            {
            $stock = lookup($row["symbol"]);
            if ($stock !== false)
                {
                $positions[] = 
                    [
                    "name" => $stock["name"],                                                                                       //we are creating a new array holding parts of the other two arrays
                    "price" => $stock["price"],
                    "shares" => $row["shares"],
                    "symbol" => strtoupper($row["symbol"]),
                    "total" => $row['shares'] * $stock['price']                                                                     //added for the end column 
                    ];
                }
            }
  
    
        $cash = CS50::query("SELECT * FROM users WHERE id = ?", $_SESSION["id"]);
        $actual_cash= $cash[0]["cash"];                                                                                             //must grab the 0 index in the array 
        
        
        render("portfolio.php", ["positions" => $positions, "title" => "Portfolio", "cash" => $actual_cash]);

?>


