<form action="index.php" method="post">
    <fieldset>
        
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script type="text/javascript">
        
        // Load the Visualization API and the piechart package.
        google.charts.load('current', {'packages':['corechart', 'table']});
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart1);
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart2);
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart3);
          
        function drawChart1() {
          var jsonData = $.ajax({
              url: "getGender.php",
              dataType: "json",
              async: false
              }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.PieChart(document.getElementById('chart_div1'));
          chart.draw(data, {'title':'Incidents per Gender', width: 400, height: 240});
        }
        
        function drawChart2() {
          var jsonData = $.ajax({
              url: "getRace.php",
              dataType: "json",
              async: false
              }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.PieChart(document.getElementById('chart_div2'));
          chart.draw(data, {'title':'Incidents per Race',width: 400, height: 240});
        }
        
        function drawChart3() {
          var jsonData = $.ajax({
              url: "getCharge.php",
              dataType: "json",
              async: false
              }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.PieChart(document.getElementById('chart_div3'));
          chart.draw(data, {'title':'Most-Charged Statutes', width: 400, height: 240});
        }

        </script>
        <table class="columns">
          <tr>
            <td><div id="chart_div1" align="center" style="border: 1px solid #ccc"></div></td>
            <td><div id="chart_div2" align="center" style="border: 1px solid #ccc"></div></td>
            <td><div id="chart_div3" align="center" style="border: 1px solid #ccc"></div></td>
          </tr>
        </table>

        <div>
        <br/>
        <div id="bottom">
        </div>
    </fieldset>
</form>

