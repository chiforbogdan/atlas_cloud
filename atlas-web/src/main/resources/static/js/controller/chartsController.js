'use strict'

atlas_app.controller('ChartsController',[ '$scope', '$filter', function($scope, $filter) {

	/* Maximum number of samples in the plot (5 minute sample rate) */
	const PLOT_MAX_SAMPLES = 288
    
	$scope.client = $scope.$parent.client;

    $scope.firewallValues = [];
    $scope.reputationValues = [];
    $scope.timeLabels = [];
    
    /* Last plot samples dates */
    $scope.lastSampleDate = {
    	firewallIngress: '',
    	firewallEgress: '',
    	systemReputation: '',
    	temperatureReputation: ''
    };

    $scope.$on('clientDataChangedEvent', function (events, data){
    	$scope.client = data;

        /* Dates at which the samples were taken */
        var dates = $scope.client.systemReputationHistory.map(x => x.date);
        angular.forEach(dates, function(value, key) {
          	$scope.timeLabels.push($filter('date')(value,'yyyy-MM-dd HH:mm:ss'));
        })

        var updateReputationPlot = false;
        /* System reputation samples */
        if ($scope.client.systemReputationHistory.length > 0) {
        	var currentSysRepDate = $scope.client.systemReputationHistory[$scope.client.systemReputationHistory.length - 1].date;
        	/* If the last value is updated, then we must update the plot */
        	if (currentSysRepDate != $scope.lastSampleDate.systemReputation) {
        		$scope.systemReputationHistory = $scope.client.systemReputationHistory.map(x => x.value).map(Number);
        		$scope.lastSampleDate.systemReputation = currentSysRepDate;
        		updateReputationPlot = true;
        	}
        }
        /* Temperature reputation samples */
        if ($scope.client.temperatureReputationHistory.length > 0) {
        	var currentTempRepDate = $scope.client.temperatureReputationHistory[$scope.client.temperatureReputationHistory.length - 1].date;
        	/* If the last value is updated, then we must update the plot */
        	if (currentTempRepDate != $scope.lastSampleDate.temperatureReputation) {
        		$scope.temperatureReputationHistory = $scope.client.temperatureReputationHistory.map(x => x.value).map(Number);
        		$scope.lastSampleDate.temperatureReputation = currentTempRepDate;
        		updateReputationPlot = true;
        	}
        }
        /* Update reputation plot is required */
        if (updateReputationPlot)
        	$scope.reputationSelectedIntervalChanged();
        
        /* Firewall ingress */
        if ($scope.client.firewallRulePassedPktsHistory.length > 0) {
        	var currentFwIngressDate = $scope.client.firewallRulePassedPktsHistory[$scope.client.firewallRulePassedPktsHistory.length - 1].date;
        	/* If the last value is updated, then we must update the plot */
        	if (currentFwIngressDate != $scope.lastSampleDate.firewallIngress) {
        		$scope.firewallRuleDroppedPktsHistory = $scope.client.firewallRuleDroppedPktsHistory.map(x => x.value).map(Number);
        		$scope.firewallRulePassedPktsHistory = $scope.client.firewallRulePassedPktsHistory.map(x => x.value).map(Number);
        		$scope.lastSampleDate.firewallIngress = currentFwIngressDate;
        		/* Update ingress plot only if ingress is selected */
        		//console.log("Selected " + $scope.firewallSelectedDirection);
        		if($scope.firewallSelectedDirection != 'egress') {
        			//console.log("GO ingress");
        			$scope.firewallIngressUpdatePlot();
        		}
        	}
        }
        
        /* Firewall egress */
        if ($scope.client.firewallTxPassedPktsHistory.length > 0) {
        	var currentFwEgressDate = $scope.client.firewallTxPassedPktsHistory[$scope.client.firewallTxPassedPktsHistory.length - 1].date;
        	/* If the last value is updated, then we must update the plot */
        	if (currentFwEgressDate != $scope.lastSampleDate.firewallEgress) {
                $scope.firewallTxDroppedPktsHistory = $scope.client.firewallTxDroppedPktsHistory.map(x => x.value).map(Number);
                $scope.firewallTxPassedPktsHistory = $scope.client.firewallTxPassedPktsHistory.map(x => x.value).map(Number);
         		$scope.lastSampleDate.firewallEgress = currentFwEgressDate;
        		/* Update egress plot only if egress is selected */
        		if($scope.firewallSelectedDirection == 'egress') {
        			$scope.firewallEgressUpdatePlot();
        		}
        	}
        }
    });

    /* Selected option for time interval (12h or 24h) */
    $scope.firewallSelectedInterval = '';
    $scope.reputationSelectedInterval = '';

    /* Ingress or egress data */
    $scope.firewallSelectedDirection = '';

    /*
    * Callback function when selected interval for firewall graph
    * is chanced or selected direction for firewall changed
    */
    $scope.firewallEgressUpdatePlot = function() {
    	if($scope.firewallSelectedInterval == 'last_day') {
            /* Get PLOT_MAX_SAMPLES/2 samples from even positions */
            var filteredFirewallTxPassedPktsHistory = $scope.firewallTxPassedPktsHistory.filter((a,i) => i % 2 === 0);
            var filteredFirewallTxDroppedPktsHistory = $scope.firewallTxDroppedPktsHistory.filter((a,i) => i % 2 === 0);
            $scope.timeLabels = $scope.allTimeLabels.filter((a,i) => i % 2 === 0);

            $scope.firewallValues[0] = filteredFirewallTxPassedPktsHistory;
            $scope.firewallValues[1] = filteredFirewallTxDroppedPktsHistory;
        } else {
            /* Last 12h (last PLOT_MAX_SAMPLES/2 values) */
            if($scope.firewallTxPassedPktsHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.firewallValues[0] = $scope.firewallTxPassedPktsHistory;
            else
            	$scope.firewallValues[0] = $scope.firewallTxPassedPktsHistory.slice($scope.firewallTxPassedPktsHistory.length - PLOT_MAX_SAMPLES / 2,
            																		$scope.firewallTxPassedPktsHistory.length);
            
            if($scope.firewallTxDroppedPktsHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.firewallValues[1] = $scope.firewallTxDroppedPktsHistory;
            else
            	$scope.firewallValues[1] = $scope.firewallTxDroppedPktsHistory.slice($scope.firewallTxDroppedPktsHistory.length - PLOT_MAX_SAMPLES / 2,
            																		 $scope.firewallTxDroppedPktsHistory.length);
        }
    }
    
    $scope.firewallIngressUpdatePlot = function() {
        if($scope.firewallSelectedInterval == 'last_day') {
            /* Get PLOT_MAX_SAMPLES/2 samples from even positions */
            var filteredFirewallRulePassedPktsHistory = $scope.firewallRulePassedPktsHistory.filter((a,i) => i % 2 === 0);
            var filteredFirewallRuleDroppedPktsHistory = $scope.firewallRuleDroppedPktsHistory.filter((a,i) => i % 2 === 0);

            $scope.firewallValues[0] = filteredFirewallRulePassedPktsHistory;
            $scope.firewallValues[1] = filteredFirewallRuleDroppedPktsHistory;
        } else {
        	/* Last 12h (last PLOT_MAX_SAMPLES/2 values) */
            if($scope.firewallRulePassedPktsHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.firewallValues[0] = $scope.firewallRulePassedPktsHistory;
            else
            	$scope.firewallValues[0] = $scope.firewallRulePassedPktsHistory.slice($scope.firewallRulePassedPktsHistory.length - PLOT_MAX_SAMPLES / 2,
            																		  $scope.firewallRulePassedPktsHistory.length);
            
            if($scope.firewallRuleDroppedPktsHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.firewallValues[1] = $scope.firewallRuleDroppedPktsHistory;
            else
            	$scope.firewallValues[1] = $scope.firewallRuleDroppedPktsHistory.slice($scope.firewallRuleDroppedPktsHistory.length - PLOT_MAX_SAMPLES / 2,
            																		   $scope.firewallRuleDroppedPktsHistory.length);
        }
    };
    
    $scope.firewallSelectedIntervalChanged = function() {
        if($scope.firewallSelectedDirection == 'egress') {
        	$scope.firewallEgressUpdatePlot();
        } else {
        	$scope.firewallIngressUpdatePlot();
        }
    };

    /* Callback function when selected interval for reputation graph is chanced */
    $scope.reputationSelectedIntervalChanged = function() {
        if($scope.reputationSelectedInterval == 'last_day') {
            /* Get PLOT_MAX_SAMPLES/2 samples from even positions */
            var filteredSystemReputationHistory = $scope.systemReputationHistory.filter((a,i) => i % 2 === 0);
            var filteredTemperatureReputationHistory = $scope.temperatureReputationHistory.filter((a,i) => i % 2 === 0);

            $scope.reputationValues[0] = filteredSystemReputationHistory;
            $scope.reputationValues[1] = filteredTemperatureReputationHistory;
        } else {
        	/* Last 12h (last PLOT_MAX_SAMPLES/2 values) */
            if($scope.systemReputationHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.reputationValues[0] = $scope.systemReputationHistory;
            else
            	$scope.reputationValues[0] = $scope.systemReputationHistory.slice($scope.systemReputationHistory.length - PLOT_MAX_SAMPLES / 2,
            																	  $scope.systemReputationHistory.length);
            
            if($scope.temperatureReputationHistory.length < PLOT_MAX_SAMPLES / 2)
                $scope.reputationValues[1] = $scope.temperatureReputationHistory;
            else
            	$scope.reputationValues[1] = $scope.temperatureReputationHistory.slice($scope.temperatureReputationHistory.length - PLOT_MAX_SAMPLES / 2,
            																		   $scope.temperatureReputationHistory.length);
        }
    };

   /* Configuration for firewall graph */
   $scope.json_firewall = {
      globals: {
          shadow: false,
          fontFamily: "Helvetica"
      },
      type: "line",
      legend: {
           layout: "x2",
           overflow: "page",
           alpha: 0.05,
           shadow: false,
           align: "right",
           marker: {
             type: "circle",
             borderColor: "none",
             size: "10px"
           },
           borderWidth: 0
      },
      scaleX: {
          zooming: true,
          label: {
              text: "Time"
          },
          guide: {
              visible: false
          },
          /*values: $scope.timeLabels, !!
          item:{
              fontAngle: -45,
              offsetX: "7px"
          }*/
      },
      scaleY: {
          label: {
              text: "Number of packets"
          }
      },
      tooltip: {
          visible: false
      },
      crosshairX: {
          scaleLabel: {
              backgroundColor: "#000000",
              fontColor: "white"
          },
          plotLabel: {
              backgroundColor: "black",
              fontColor: "#ffffff"
          }
      },
      plot: {
          aspect: "spline",
          marker: {
              visible: true
          }
      },
      series: [{
          text: "Passed Packets",
          lineColor: "#29A2CC"
      }, {
          text: "Dropped Packets",
          lineColor: "#D31E1E"
      }]
    };

   /* Configuration for reputation graph */
   $scope.json_reputation = {
      globals: {
          shadow: false,
          fontFamily: "Helvetica"
      },
      type: "line",
      legend: {
           layout: "x2",
           overflow: "page",
           alpha: 0.05,
           shadow: false,
           align: "right",
           marker: {
             type: "circle",
             borderColor: "none",
             size: "10px"
           },
           borderWidth: 0
      },
      scaleX: {
          zooming: true,
          label: {
              text: "Time"
          },
          guide: {
              visible: false
          }
      },
      scaleY: {
          label: {
              text: "Reputation value"
          }
      },
      tooltip: {
          visible: false
      },
      crosshairX: {
          scaleLabel: {
              backgroundColor: "#000000",
              fontColor: "white"
          },
          plotLabel: {
              backgroundColor: "black",
              fontColor: "#ffffff"
          }
      },
      plot: {
          aspect: "spline",
          marker: {
              visible: false
          }
      },
      series: [{
          text: "System reputation",
          lineColor: "#0B614B"
      }, {
          text: "Temperature reputation",
          lineColor: "#DF7401"
      }]
    };
}]);