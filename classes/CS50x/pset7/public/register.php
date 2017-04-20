<?php

    // configuration
    require("../includes/config.php");

    // if user reached page via GET (as by clicking a link or via redirect)
    if ($_SERVER["REQUEST_METHOD"] == "GET")
    {
        // else render form
        render("register_form.php", ["title" => "Register"]);
    }

    // else if user reached page via POST (as by submitting a form via POST)
    else if ($_SERVER["REQUEST_METHOD"] == "POST")
        {
        // validate submission
        if (empty($_POST["username"]))
            {
            apologize("You must provide your username.");
            }
        else if (empty($_POST["password"]))
            {
            apologize("You must provide your password.");
            }
        else if (empty($_POST["confirmation"]))
            {
            apologize("You must provide a confirmation.");
            }
        else if ($_POST["password"] !== $_POST["confirmation"])
            {
            apologize("...password and confirmation do not match");
            }

        //call this to insert new user into database
        $insert = CS50::query("INSERT IGNORE INTO users (username, hash, cash) VALUES(?, ?, 10000.0000)", $_POST["username"], password_hash($_POST["password"], PASSWORD_DEFAULT));
        
        // if we found user
        if (count($insert) == 1)
        {
            $rows = CS50::query("SELECT LAST_INSERT_ID() AS id");
            $id = $rows[0]["id"];

                // remember that user's now logged in by storing user's ID in session
                $_SESSION["id"] = $id["id"];

                // redirect to portfolio
                redirect("index.php");
            
        }
        // else apologize
        apologize("Invalid username and/or password.");
  
        }

?>