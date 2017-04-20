<?php

    // configuration
    require("../includes/config.php");
    
        // validate submission
        //if (empty($_POST["selling"]))
        //    {
        //    apologize("You must select a stock to sell.");
         //   }
            
        //see what we have to sell
        //$rows = CS50::query("SELECT symbol FROM portfolio WHERE user_id = ?", $_SESSION["id"]);
   
            //if(isset($_POST['submit'])){
            //$selected_val = $_POST['list'];
            //print($selected_val);

        if(isset($_POST['button']))
            {

            $stock = lookup($_POST['list']);
            print_r($stock);
            $price = $stock["price"];
            print($price);
            print("==============================>");
    
            
            $cash = CS50::query("SELECT cash FROM users WHERE id = ?", $_SESSION["id"]);
            $actual_cash= $cash[0]["cash"];
            print_r($actual_cash);

            $all_shares = CS50::query("SELECT * FROM portfolio WHERE user_id = ? AND symbol = ?", $_SESSION["id"], $_POST["list"]);
            $shares_available = $all_shares[0]["shares"];
            print("<=============================shares available>>>");
            print_r($shares_available);
            
            //$del = "DELETE FROM portfolio WHERE user_id = ?", $_SESSION["id"];
            
            $new_cash = $actual_cash + ($shares_available * $price);
            print("=============================new cash>>>");
            print($new_cash);
            
            
            CS50::query("UPDATE users SET cash = ? WHERE id = ?", $new_cash, $_SESSION["id"]);
            CS50::query("DELETE FROM portfolio WHERE user_id = ? AND symbol = ?", $_SESSION["id"], $_POST["list"]);
                                                                                                                                    //add for history
            CS50::query("INSERT INTO history (user_id, transaction, time, symbol, shares, price) VALUES ('".$_SESSION['id']."', 'SELL', NOW(), '".$_POST['list']."', '$shares_available', '$price')");

            if(isset($_POST['button']))
                {
                redirect("index.php");
                }
            
            }

       
        render("sell_form.php", ["title" => "Sell"]);
        

?>