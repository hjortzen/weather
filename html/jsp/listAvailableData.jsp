<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
    	<script type="text/javascript" src="../js/data_fetch.js"></script>
    </head>
    <body>
    	This is hopefully a full listing of weather data
    	<table>
    		<thead>
	    		<tr>
	    			<th>Time</th>
	    			<th>Wind avg.</th>
	    			<th>Wind max</th>
	    			<th>Direction</th>
	    			<th>Temp</th>
	    			<th>Snow</th>
	    		</tr>
    		</thead>
            <tbody>
                <c:forEach var="weather" items="${observations}" varStatus="status">
                    <tr>
                        <td>${weather.formattedDate}</td>
                        <td>${weather.windSpeed}</td>
                        <td>TBD</td>
                        <td>${weather.windDirection}</td>
                        <td>${weather.temperature}</td>
                        <td>${weather.snowDepth}</td>
                    </tr>
                </c:forEach>
            </tbody>
    	</table>
    </body>
</html>