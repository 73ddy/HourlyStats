/**
 * Data fetching and manipulation goes here
 */
'use strict';
// would have used d.getTimezoneOffset() * 60000; instead if the GMT time was being supplied.
var timezoneCorrection = 19800000;
$(document).ready(function() {
	$.ajax({
		url : '/stats/statistics',
		// allow caching if the statistics don't change often
		cache : false,
		type : 'GET',
		success : function(statistics) {
			drawHighStockChart(statistics);	
		}
	});
});

/**
 * Draw out the histogram on page.
 * 
 * @param queryFrequencyByHour
 */
function drawHighStockChart(queryFrequencyByHour) {
	var data = prepareDataForDisplay(addAllMissingHours(queryFrequencyByHour));
	$(function() {
		$('#data_canvas').highcharts({
		    
		    chart: {
	            renderTo: 'data_canvas',
	            defaultSeriesType: 'column',
	            zoomType: 'xy'
	        },
	        title: {
	            text: 'Hourly distribution'
	        },
	        subtitle: {
	            text: 'Select area to zoom in'
	        },
	        credits: {
	            enabled: false,
	        },
	        xAxis: {
	            categories: data.xAxis,
	            title: {
	                enabled: true,
	                margin: 15
	            },
	            label: {
	            	rotation: -90
	            }
	        },

	        yAxis: {
	            title: {
	                text: 'Query Count'
	            }
	        },
	        legend: {
	            shadow: true
	        },
	        tooltip: {
	            useHTML: true,
	            headerFormat: '<small>{point.key}</small><table>',
	            pointFormat: '<tr><td style="color: {series.color}">{series.name}:{point.y} </td>',
	            footerFormat: '</table>',
	            valueDecimals: 2,
	            crosshairs: [{
	                width: 1,
	                color: 'gray'},
	            {
	                width: 1,
	                color: 'gray'}]
	        },
	        plotOptions: {
	            column: {
	            	stacking: 'normal',
	            	plotWidth: 50,
	                borderWidth: 0.5,
	                shadow:true,
	                pointPadding:0,
	                groupPadding:0,
	            }
	        },
	        series: [{
		            name: 'Query Count',
		            data: data.yAxis
	            },
	        ],
		    
		    scrollbar: {
	            enabled:true,
				barBackgroundColor: 'gray',
				barBorderRadius: 7,
				barBorderWidth: 0,
				buttonBackgroundColor: 'gray',
				buttonBorderWidth: 0,
				buttonArrowColor: 'yellow',
				buttonBorderRadius: 7,
				rifleColor: 'yellow',
				trackBackgroundColor: 'white',
				trackBorderWidth: 1,
				trackBorderColor: 'silver',
				trackBorderRadius: 7
		    }
		});
	});
}

/**
 * Prepares the data for the histogram.
 * 
 * @param queryFrequencyByHour
 * @returns json with x and y axis data
 */
function prepareDataForDisplay(queryFrequencyByHour){
	var data = {
			xAxis: [],
			yAxis: []
	};
	var xValue, date, day = -1, curDay;
	
	for(var prop in queryFrequencyByHour) {
		date = new Date(prop * 3600000 - timezoneCorrection);
		curDay = date.getDate();
		xValue = date.getHours();
		xValue = xValue + " - " + (xValue + 1);
		
		if (day != curDay) {
			xValue = date.getFullYear() + '/' + (date.getMonth() + 1) + "/" + curDay + " " + xValue;
			day = curDay;
		}
		
		data.xAxis.push(xValue);
		data.yAxis.push(queryFrequencyByHour[prop]);
	}
	
	return data;
}

/**
 * Adds all the missing hours in the sorted hours with value 0.
 * 
 * @param queryFrequencyByHour
 * @returns
 */
function addAllMissingHours(queryFrequencyByHour) {
	var prevEpochHour = 0;
	var hour;
	// add all the missing hours
	for (var prop in queryFrequencyByHour) {
		prop = parseInt(prop);
		if (prevEpochHour != 0) {
			hour = prevEpochHour + 1;
			while (hour != prop) {
				queryFrequencyByHour[hour++] = 0;
			}
		}
		
		prevEpochHour = prop;
	}
	return queryFrequencyByHour;
}