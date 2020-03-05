var app = angular.module('atlas', []);
app.controller('GatewayController',[ '$scope', '$interval', '$window', 'GatewayService', function($scope, $interval, $window, GatewayService) {

    $scope.gateways = [];
    $scope.gateway = { identity: '', psk: '' };
    $scope.clients = [];

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

        console.log('Saving New Gateway', $scope.gateway);

        $scope.status = 1;

        GatewayService.createGateway($scope.gateway);

        $scope.reset();
    };

    $scope.showClients = function(gateway) {
           alert('aaa');
           $window.location.href = 'gateway.html';

           $scope.clients = fetchAllClients(gateway.psk);
    };


    $scope.reset = function() {
        $scope.gateway = { identity: '', psk: '' };
        $scope.searchForm.$setPristine(); //reset Form
    };

}]);