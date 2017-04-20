<!DOCTYPE html>

<html>

    <head>

        <!-- http://getbootstrap.com/ -->
        <link href="/css/bootstrap.min.css" rel="stylesheet"/>

        <link href="/css/styles.css" rel="stylesheet"/>

        <?php if (isset($title)): ?>
            <title>CS50 Final: <?= htmlspecialchars($title) ?></title>
        <?php else: ?>
            <title>CS50 Final</title>
        <?php endif ?>

        <!-- https://jquery.com/ -->
        <script src="/js/jquery-1.11.3.min.js"></script>

        <!-- http://getbootstrap.com/ -->
        <script src="/js/bootstrap.min.js"></script>

        <script src="/js/scripts.js"></script>
        

    </head>

    <body>
    <div>
    <br/>
    <a href="https://data.montgomerycountymd.gov/Public-Safety/Traffic-Violations/4mse-ku6q/data"><img alt="Source" src="/img/logo3.gif"/>
    </div>
        <div class="container">

            <div id="top">
                <div>
                    <a href="/"><img alt="CS50 Final" src="/img/logo.png"/></a>
                </div>
                <?php if (!empty($_SESSION["id"])): ?>
                    <ul class="nav nav-pills">
                        <li><a href="time.php">Violations by Time</a></li>
                        <li><a href="year.php">Violations per Year</a></li>
                        <li><a href="logout.php"><strong>Log Out</strong></a></li>
                    </ul>
                <?php endif ?>
            </div>

            <div id="middle">
