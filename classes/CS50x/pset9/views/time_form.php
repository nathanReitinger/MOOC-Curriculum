
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

        function drawChart1() {
          var jsonData = $.ajax({
            url: "getTime.php",
            dataType: "json",
            async: false
            }).responseText;
              
          // Create our data table out of JSON data loaded from server.
          var data = new google.visualization.DataTable(jsonData);
          
                  var options = {
                  'legend': 'none',
                  'title': 'Traffic Violations per Time-of-Day',
                  'width': 1200,
                  'height': 400,
                  'tooltip' : {                   //since we use only 1 date (in order to stack the times), the informational date is incorrect
                      trigger: 'none'
                    }
                    };
    
          // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.ScatterChart(document.getElementById('chart_div1'));
          chart.draw(data, options);
        }
    
        </script>
        <div id="chart_div1"></div>
        <div> 
        <div id="bottom">
            
        </div>
    </fieldset>
