'use strict'

atlas_app.controller('ChartsController',[ '$scope', 'GatewayService', function($scope, GatewayService) {
    $scope.myJson3 = {
       type: "bar",
       title: {
          text: "Clients distribution over gateways",
          fontColor: "#7E7E7E",
          backgroundColor: "none",
          fontSize: "22px",
          alpha: 1,
          adjustLayout: true,
       },
       legend: {
          layout: "x2",
          overflow: "page",
          alpha: 0.05,
          shadow: false,
          align: "center",
          adjustLayout: true,
          marker: {
            type: "circle",
            borderColor: "none",
            size: "10px"
          },
          borderWidth: 0,
          toggleAction: "hide"
       },
       plotarea: {
          adjustLayout: true
       },
       scaleX: {
           label: {
              text: "Gateway id number",
           },
           itemsOverlap: true,
           maxItems: 1000
       },
       scaleY: {
          label: {
              text: "Clients number",
          }
       },
       series: [{
           values: [20, 40, 25, 50, 15, 45, 33, 34, 20, 40, 25, 50, 15, 45, 33, 34],
           backgroundColor: "#003399",
           text: "Total",
         },
         {
           values: [5, 30, 21, 18, 59, 50, 28, 33, 20, 15, 20, 10, 0, 40, 31, 30],
           backgroundColor: "#00b300",
           text: "Active",
         }]
    };

    var reputationValues = {"systemReputation":["0.35","0.4","0.25","0.15","0.12"],"temperatureReputation":["0.31","0.3","0.2","0.22","0.18"]};
    $scope.myJson2 = {
         type: 'line',
         series : [{
            text: "System",
            values: reputationValues.systemReputation.map(Number)
         },
         {
            text: "Temperature",
            values: reputationValues.temperatureReputation.map(Number)
         }]
     };
}]);