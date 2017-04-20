
<form action="index.php" method="post">

    <fieldset>
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script type="text/javascript">
        
        // Load the Visualization API and the piechart package.
        google.charts.load('current', {'packages':['corechart']});
        
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart1);
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart2);
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(drawChart3);

        function drawChart1() {
          var jsonData = $.ajax({
            url: "getYear_day.php",
            dataType: "json",
            async: false
            }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
          
                  var options = {
                  'legend': 'none',
                  'title': 'Traffic Violations per Day',
                  'trendlines': { 0: {'type':'exponential','color':'yellow',} },
                  'width': 1200,
                  'height': 400,
                   };
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.ScatterChart(document.getElementById('chart_div1'));
          chart.draw(data, options);
        }
        
        function drawChart2() {
          var jsonData = $.ajax({
            url: "getYear_month.php",
            dataType: "json",
            async: false
            }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
          
                  var options = {
                  'legend': 'none',
                  'title': 'Traffic Violations per Month',
                  'trendlines': { 0: {'type':'exponential','color':'yellow',} },
                  'width': 1200,
                  'height': 400,
                    };
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.ColumnChart(document.getElementById('chart_div2'));
          chart.draw(data, options);
        }
    
        </script>
        
        <div id="chart_div1"></div>
        <div id="chart_div2"></div>
        <div id="chart_div3"></div>
        <div> 
        <div id="bottom">
            
        </div>
    </fieldset>
