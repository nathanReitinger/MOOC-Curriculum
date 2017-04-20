<?php

    // configuration
    require("../includes/config.php");
    
        if(!empty($_POST['symbol']) && !empty($_POST['shares']) && (preg_match("/^\d+$/", $_POST["shares"]) == true))
            {
 
            $symbol = lookup($_POST['symbol']);
            $price = $symbol['price'];
            $no_shares = $_POST['shares'];
            print_r($symbol);
            print($no_shares);
            print(",");
            print($price);
            
            
            
            
            $total = $price * $no_shares;
            print(",");
            print($total);
            
            
            
            $cash = CS50::query("SELECT cash FROM users WHERE id = ?", $_SESSION["id"]);
            $actual_cash= $cash[0]["cash"];
            print(",");
            print_r($actual_cash);
            
            $afford = $actual_cash - $total;
            print(",");
            print_r($afford);
            
            
            if ($afford >= 0 )
                {
                CS50::query("UPDATE users SET cash = ? WHERE id = ?", $afford, $_SESSION["id"]);
                CS50::query("INSERT INTO portfolio (user_id, symbol, shares) VALUES ('".$_SESSION['id']."', '".$_POST['symbol']."', '".$_POST['shares']."') ON DUPLICATE KEY UPDATE shares = shares + VALUES(shares)");
                                                                                                                                    //add for history
                CS50::query("INSERT INTO history (user_id, transaction, time, symbol, shares, price) VALUES ('".$_SESSION['id']."', 'BUY', NOW(), '".$_POST['symbol']."', '$no_shares', '$price')");
                redirect("index.php");
                }
            else if ($afford <= 0 )
                {
                apologize("You are unable to afford that transaction!"); 
                }
            
            }

       
        render("buy_form.php", ["title" => "Sell"]);
        

?>
