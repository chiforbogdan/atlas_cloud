'use strict'

atlas_app.controller('ChartsController',[ '$scope', 'GatewayService', function($scope, GatewayService) {

    var allValues1 = [0.68, 0.54, 0.1, 0.46, 0.39, 0.88, 0.03, 0.27, 0.54, 0.29, 0.81, 0.91, 0.79, 0.63, 0.17, 0.21, 0.49, 0.25, 0.05, 0.92, 0.07, 0.32, 0.24, 0.53, 0.18, 0.46, 0.31, 0.26, 0.54, 0.98, 0.77, 0.81, 0.29, 0.38, 0.15, 0.45, 0.21, 0.96, 0.24, 0.97, 0.84, 0.98, 0.28, 0.81, 0.8, 0.8, 0.88, 0.08, 0.25, 0.87, 0.41, 0.55, 0.9, 0.36, 0.51, 0.5, 0.57, 0.05, 0.15, 0.65, 0.54, 0.76, 0.48, 0.26, 0.08, 0.4, 0.94, 0.41, 0.18, 0.33, 0.23, 0.22, 0.51, 0.38, 0.74, 0.35, 0.72, 0.96, 0.84, 0.61, 0.82, 0.68, 0.07, 0.07, 0.51, 0.46, 0.75, 0.48, 0.98, 0.14, 0.44, 0.86, 0.42, 0.87, 0.46, 0.66, 0.17, 0.15, 0.25, 0.51, 0.56, 0.17, 0.13, 0.11, 0.98, 0.3, 0.69, 0.85, 0.51, 0.04, 0.49, 0.19, 0.33, 0.85, 0.83, 0.34, 0.02, 0.92, 0.72, 0.77, 0.75, 0.32, 0.13, 0.3, 0.44, 0.28, 0.75, 0.93, 0.09, 0.78, 0.69, 0.76, 0.14, 0.56, 0.47, 0.24, 0.29, 0.54, 0.73, 0.65, 0.63, 0.51, 0.75, 0.1, 0.24, 0.67, 0.56, 0.91, 0.82, 0.94, 0.7, 0.68, 0.15, 0.13, 0.16, 0.58, 0.07, 0.21, 0.21, 0.47, 0.95, 0.44, 0.48, 0.07, 0.26, 0.03, 0.45, 0.9, 0.5, 0.89, 0.27, 0.71, 0.54, 0.02, 0.54, 0.32, 0.49, 0.94, 0.73, 0.13, 0.89, 0.83, 0.44, 0.75, 0.14, 0.8, 0.29, 0.95, 0.04, 0.17, 0.47, 0.09, 0.12, 0.68, 0.12, 0.77, 0.17, 0.68, 0.64, 0.41, 0.09, 0.11, 0.81, 0.08, 0.63, 0.6, 0.69, 0.17, 0.46, 1.0, 0.07, 0.1, 0.41, 0.2, 0.72, 0.23, 0.07, 0.07, 0.69, 0.45, 0.06, 0.66, 0.38, 0.29, 0.74, 0.02, 0.77, 0.51, 0.57, 0.96, 0.96, 0.57, 0.65, 0.4, 0.47, 0.33, 0.1, 0.24, 0.7, 0.52, 0.66, 0.66, 0.73, 0.85, 0.47, 0.91, 0.62, 0.2, 0.57, 0.08, 0.43, 0.57, 0.9, 0.82, 0.15, 0.84, 0.36, 0.27, 0.7, 0.72, 0.34, 0.89, 0.33, 0.19, 0.22, 0.2, 0.61, 0.88, 0.98, 0.8, 0.3, 0.66, 0.17, 0.58, 0.62, 0.64, 0.97, 0.24, 0.98, 0.99, 0.26, 0.65, 0.32, 0.87, 0.59, 0.47, 0.42 ];
    var allValues2 = [0.72, 0.01, 0.08, 0.38, 0.51, 0.33, 0.84, 0.83, 0.86, 0.35, 0.43, 0.77, 0.63, 0.69, 0.5, 0.29, 0.78, 0.38, 0.68, 0.19, 0.93, 0.05, 0.7, 0.88, 0.72, 0.22, 0.38, 0.29, 0.18, 0.12, 0.75, 0.16, 0.49, 0.74, 0.04, 0.43, 0.95, 0.77, 0.95, 0.1, 0.23, 0.52, 0.77, 0.49, 0.45, 0.48, 0.81, 0.41, 0.16, 0.51, 0.86, 0.45, 0.81, 0.5, 0.72, 0.62, 0.4, 0.82, 0.1, 0.92, 0.31, 0.95, 0.73, 0.2, 0.45, 0.53, 0.4, 0.25, 0.39, 0.02, 0.74, 0.72, 0.23, 0.95, 0.9, 0.19, 0.54, 0.21, 0.48, 0.88, 0.33, 0.38, 0.45, 1.0, 0.96, 0.82, 0.83, 0.21, 0.59, 0.33, 1.0, 0.6, 0.14, 0.02, 0.14, 0.8, 0.87, 0.69, 0.36, 0.87, 0.66, 0.59, 0.12, 0.26, 0.63, 0.88, 0.68, 0.66, 0.99, 0.01, 0.67, 0.94, 0.35, 0.64, 0.91, 0.35, 0.38, 0.16, 0.59, 0.67, 0.79, 0.35, 0.85, 0.63, 0.79, 0.35, 0.58, 0.25, 0.35, 0.53, 0.21, 0.98, 0.9, 0.6, 0.84, 0.98, 0.52, 0.29, 0.51, 0.37, 0.05, 0.81, 0.89, 0.61, 0.15, 0.19, 0.46, 0.01, 0.27, 0.84, 0.8, 0.99, 0.66, 0.14, 0.93, 0.68, 0.33, 0.38, 0.43, 0.51, 0.39, 0.66, 0.99, 0.09, 0.33, 0.93, 0.71, 0.19, 0.27, 0.28, 0.56, 1.0, 0.3, 0.49, 0.54, 0.7, 0.34, 0.84, 0.01, 0.88, 0.16, 0.85, 0.66, 0.59, 0.35, 0.61, 0.4, 0.35, 0.68, 0.64, 0.26, 0.6, 0.33, 0.47, 1.0, 0.21, 0.62, 0.13, 0.07, 0.81, 0.91, 0.81, 0.33, 0.69, 0.55, 0.11, 0.82, 0.98, 0.66, 0.6, 0.29, 0.32, 0.04, 0.85, 0.25, 0.35, 0.99, 0.17, 0.41, 0.5, 0.38, 0.04, 0.2, 0.81, 0.48, 0.5, 0.3, 0.47, 0.64, 0.23, 0.58, 0.91, 0.93, 0.68, 0.92, 0.67, 0.98, 0.07, 0.87, 0.31, 0.71, 0.69, 0.62, 0.7, 0.89, 0.79, 0.87, 0.03, 0.25, 0.44, 0.39, 0.77, 0.95, 0.94, 0.76, 0.07, 0.2, 0.07, 0.85, 0.5, 0.19, 0.87, 0.86, 0.48, 0.19, 0.67, 0.35, 0.08, 0.3, 0.93, 0.38, 0.23, 0.72, 0.24, 0.23, 0.73, 0.49, 0.93, 0.51, 0.6, 0.72, 0.66, 0.38, 0.43, 0.4, 0.06, 0.7, 0.72 ];

    /*
    *  Data intervals that appear in the graphs,
    *  by default the last 12h data is showed
    */
    $scope.firewallValues = [];
    $scope.firewallValues[0] = allValues1.slice(0,144);
    $scope.firewallValues[1] = allValues2.slice(0,144);

    $scope.reputationValues = [];
    $scope.reputationValues[0] = allValues1.slice(0,144);
    $scope.reputationValues[1] = allValues2.slice(0,144);

    /* Selected option for time interval (12h or 24h) */
    $scope.firewallSelectedInterval = '';
    $scope.reputationSelectedInterval = '';

    /* Callback function when selected interval for firewall graph is chanced */
    $scope.firewallSelectedIntervalChanged = function() {
        if($scope.firewallSelectedInterval == 'last_day') {
            $scope.firewallValues[0] = allValues1.slice(145,287);
            $scope.firewallValues[1] = allValues2.slice(145,287);
        }
        else {
            $scope.firewallValues[0] = allValues1.slice(0,144);
            $scope.firewallValues[1] = allValues2.slice(0,144);
        }
    };

    /* Callback function when selected interval for reputation graph is chanced */
    $scope.reputationSelectedIntervalChanged = function() {
        if($scope.reputationSelectedInterval == 'last_day') {
            $scope.reputationValues[0] = allValues1.slice(144,200);
            $scope.reputationValues[1] = allValues2.slice(144,200);
        }
        else {
            $scope.reputationValues[0] = allValues1.slice(0,144);
            $scope.reputationValues[1] = allValues2.slice(0,144);
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
          }
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