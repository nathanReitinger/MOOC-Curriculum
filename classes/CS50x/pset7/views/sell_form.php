<form action="sell.php" method="post">
    <fieldset>
        <div class=class="form-control">
            <select name="list">
                <option disabled selected value =''> Sell it! </option>
                <?php
                $rows = CS50::query("SELECT symbol FROM portfolio WHERE user_id = ?", $_SESSION["id"]);
                foreach($rows as $row)
                    {
                    echo ("<option value = '$row[symbol]'> $row[symbol] </option>");
                    }
                ?>
            </select>
<br>
<br>
        </div>
        <div class="form-group">
            <button class="btn btn-default" type="submit" name="button"  value="sell">
                Sell
            </button>
        </div>
        <div>
        </div>
    </fieldset>
</form>

