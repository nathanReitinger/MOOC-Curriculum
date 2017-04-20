<?php

    // configuration
    require("../includes/config.php");

        if(isset($_POST['injection']))
            {
            $injection = $_POST['injection'];
            
            
            $cash = CS50::query("SELECT cash FROM users WHERE id = ?", $_SESSION["id"]);
            $actual_cash= $cash[0]["cash"];

            
            $total = $actual_cash + $injection;
            
            if ($total <= 0)
                {
                apologize("Try again!"); 
                }
            
            if ($total > 0)
                {
                CS50::query("UPDATE users SET cash = ? WHERE id = ?", $total, $_SESSION["id"]);
                CS50::query("INSERT INTO history (user_id, transaction, time, symbol, price) VALUES ('".$_SESSION['id']."', 'ADDITIONAL FINANCING', NOW(), '--', '$injection')");
                redirect("index.php");
                }
            }
            
        render("money_form.php", ["title" => "Extra Capital"]);
        

?>