'use strict'

atlas_app.controller('ChartsController',[ '$scope', '$filter', function($scope, $filter) {

    $scope.client = $scope.$parent.client;

    $scope.firewallValues = [];
    $scope.reputationValues = [];
    $scope.timeLabels = [];

    $scope.$on('clientDataChangedEvent', function (events, data){
      	$scope.client = data;

        /* Dates at which the samples were taken */
        var dates = $scope.client.systemReputationHistory.map(x => x.date);
        angular.forEach(dates, function(value, key) {
          	$scope.timeLabels.push($filter('date')(value,'yyyy-MM-dd HH:mm:ss'));
        })

        /* Reputation samples */
        $scope.systemReputationHistory = $scope.client.systemReputationHistory.map(x => x.value).map(Number);
        $scope.temperatureReputationHistory = $scope.client.temperatureReputationHistory.map(x => x.value).map(Number);

        /* Firewall ingress */
        $scope.firewallRuleDroppedPktsHistory = $scope.client.firewallRuleDroppedPktsHistory.map(x => x.value).map(Number);
        $scope.firewallRulePassedPktsHistory = $scope.client.firewallRulePassedPktsHistory.map(x => x.value).map(Number);

        /* Firewall egress */
        $scope.firewallTxDroppedPktsHistory = $scope.client.firewallTxDroppedPktsHistory.map(x => x.value).map(Number);
        $scope.firewallTxPassedPktsHistory = $scope.client.firewallTxPassedPktsHistory.map(x => x.value).map(Number);

        /* Update graphs -> new data came */
        $scope.firewallSelectedIntervalChanged();
        $scope.reputationSelectedIntervalChanged();
    });

    /* Selected option for time interval (12h or 24h) */
    $scope.firewallSelectedInterval = '';
    $scope.reputationSelectedInterval = '';

    /* Ingress or egress data */
    $scope.selectedDirection = '';

    /*
    * Callback function when selected interval for firewall graph
    * is chanced or selected direction for firewall changed
    */
    $scope.firewallSelectedIntervalChanged = function() {
        if($scope.selectedDirection == 'egress') {
            if($scope.firewallSelectedInterval == 'last_day') {
                /* Get 144 samples from even positions */
                var filteredFirewallTxPassedPktsHistory = $scope.firewallTxPassedPktsHistory.filter((a,i) => i % 2 === 0);
                var filteredFirewallTxDroppedPktsHistory = $scope.firewallTxDroppedPktsHistory.filter((a,i) => i % 2 === 0);
                //$scope.timeLabels = $scope.allTimeLabels.filter((a,i) => i % 2 === 0);

                $scope.firewallValues[0] = filteredFirewallTxPassedPktsHistory;
                $scope.firewallValues[1] = filteredFirewallTxDroppedPktsHistory;
            }
            else {
                /* Last 12h [144, end] */
                if($scope.firewallTxPassedPktsHistory.length > 90)
                    $scope.firewallValues[0] = $scope.firewallTxPassedPktsHistory.slice(90, $scope.firewallTxPassedPktsHistory.length);
                if($scope.firewallTxDroppedPktsHistory.length > 90)
                    $scope.firewallValues[1] = $scope.firewallTxDroppedPktsHistory.slice(90, $scope.firewallTxDroppedPktsHistory.length);
            }
        }
        else {
            if($scope.firewallSelectedInterval == 'last_day') {
                /* Get 144 samples from even positions */
                var filteredFirewallRulePassedPktsHistory = $scope.firewallRulePassedPktsHistory.filter((a,i) => i % 2 === 0);
                var filteredFirewallRuleDroppedPktsHistory = $scope.firewallRuleDroppedPktsHistory.filter((a,i) => i % 2 === 0);

                $scope.firewallValues[0] = filteredFirewallRulePassedPktsHistory;
                $scope.firewallValues[1] = filteredFirewallRuleDroppedPktsHistory;
            }
            else {
                /* Last 12h [144, end] */
                if($scope.firewallRulePassedPktsHistory.length > 90)
                    $scope.firewallValues[0] = $scope.firewallRulePassedPktsHistory.slice(90, $scope.firewallRulePassedPktsHistory.length);
                if($scope.firewallRuleDroppedPktsHistory.length > 90)
                    $scope.firewallValues[1] = $scope.firewallRuleDroppedPktsHistory.slice(90, $scope.firewallRuleDroppedPktsHistory.length);
            }
        }
    };

    /* Callback function when selected interval for reputation graph is chanced */
    $scope.reputationSelectedIntervalChanged = function() {
        if($scope.reputationSelectedInterval == 'last_day') {
            /* Get 144 samples from even positions */
            var filteredSystemReputationHistory = $scope.systemReputationHistory.filter((a,i) => i % 2 === 0);
            var filteredTemperatureReputationHistory = $scope.temperatureReputationHistory.filter((a,i) => i % 2 === 0);

            $scope.reputationValues[0] = filteredSystemReputationHistory;
            $scope.reputationValues[1] = filteredTemperatureReputationHistory;
        }
        else {
            /* Last 12h [144, end] */
            if($scope.systemReputationHistory.length > 90)
                $scope.reputationValues[0] = $scope.systemReputationHistory.slice(90, $scope.systemReputationHistory.length);
            if($scope.temperatureReputationHistory.length > 90)
                $scope.reputationValues[1] = $scope.temperatureReputationHistory.slice(90, $scope.temperatureReputationHistory.length);
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