<form action="quote.php" method="post">
    <fieldset>
        <div class="form-group">
            <?php $stock = lookup($_POST["symbol"]); ?>
            A share of <?php print($stock["name"]);?> (<?php print($stock["symbol"]);?>) will cost <?php print("$" . $stock["price"]);?>.
        </div>
    </fieldset>
</form>
