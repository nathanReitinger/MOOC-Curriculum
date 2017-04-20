<?php

    // configuration
    require("../includes/config.php");

    
    // if user reached page via GET (as by clicking a link or via redirect)
    if ($_SERVER["REQUEST_METHOD"] == "GET")
        {
        // else render form
        render("1.submit_form.php", ["title" => "Quote"]);
        }

    // else if user reached page via POST (as by submitting a form via POST)
    else if ($_SERVER["REQUEST_METHOD"] == "POST")
        {
        $stock = lookup($_POST["symbol"]);
        // validate submission
        if (empty($_POST["symbol"]))
            {
            apologize("please enter a symbol.");
            }
        else if ($stock["price"] == 0)
            {
            apologize("Invalid stock symbol.");
            }
        else if ($stock["price"] > 0)
            {
            render("2.price_form.php", ["title" => "Price"]);
            }
        }
?>
