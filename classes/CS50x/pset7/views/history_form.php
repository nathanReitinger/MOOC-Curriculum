<form action="history.php" method="post">
    <fieldset>
    <div> 
        <table class="table table-responsive" >
            <thead>
                <tr align="center">
                    <th style="text-align:center">Transaction</th>
                    <th style="text-align:center">Time</th>
                    <th style="text-align:center">Symbol</th>
                    <th style="text-align:center">Shares</th>
                    <th style="text-align:center">Price</th>
                </tr>
            </thead>
            <tbody>
            <?php foreach ($rows as $row)
                    {  
                    $row["symbol"] = strtoupper($row["symbol"]);
                    echo("<tr>");
                    echo("<td>{$row['transaction']}</td>");
                    echo("<td>{$row['time']}</td>");
                    echo("<td>{$row['symbol']}</td>");
                    echo("<td>{$row['shares']}</td>");
                    echo("<td>&#36;{$row['price']}</td>");
                    echo("</tr>");
                    }
                ?>
            </tbody>
            </table>
            </table>
    </div>
    </fieldset>
</form>
