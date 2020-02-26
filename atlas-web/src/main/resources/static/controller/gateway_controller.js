var app = angular.module('atlas', []);
app.controller('GatewayController',[ '$scope', '$interval', 'GatewayService', function($scope, $interval, GatewayService) {

    $scope.gateways = [];
    $scope.gateway = { identity: '', psk: '' };

    fetchAllGateways();

     $interval(function() {
            fetchAllGateways()
         }, 2000)

    function fetchAllGateways() {
        GatewayService.fetchAllGateways()
           .then(
              function (d) {
                  $scope.gateways = d;
               },
               function (errResponse) {
                  console.error('Error while fetching Users');
               }
           );
    }

    $scope.submit = function() {
        $scope.reset();
    };

    $scope.reset = function() {
        $scope.gateway = { identity: '', psk: '' };
        $scope.searchForm.$setPristine(); //reset Form
    };

}]);