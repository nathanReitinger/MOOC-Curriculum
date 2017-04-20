<form action="index.php" method="post">
    <fieldset>
    <div> 
        <table class="table table-responsive" >
            <thead>
                <tr align = "center">
                    <th style="text-align:center">Symbol</th>
                    <th style="text-align:center">Name</th>
                    <th style="text-align:center">Shares</th>
                    <th style="text-align:center">Price</th>
                    <th style="text-align:center">TOTAL</th>
                </tr>
            </thead>
            <tbody>
            <?php foreach ($positions as $position)
                    {   
                    echo("<tr>");
                    echo("<td>{$position['symbol']}</td>");
                    echo("<td>{$position['name']}</td>");
                    echo("<td>{$position['shares']}</td>");
                    echo("<td>&#36;{$position['price']}</td>");
                    echo("<td>&#x24;{$position['total']}</td>");
                    echo("</tr align='left'>");
                    }
                ?>
            </tbody>
            </table>
        </div>
        
        <div id="bottom">

         <h4> You have this much left to play with:  </h4>  <?php print("<h1>$ $cash</h1>");?> 
        
        <a href="money.php">Inject Capital!</a>
    </div>
    </fieldset>
</form>
