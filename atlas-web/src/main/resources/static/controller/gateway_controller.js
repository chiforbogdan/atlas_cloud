var app = angular.module('atlas', []);
app.controller('GatewayController',[ '$scope', '$interval', 'GatewayService', function($scope, $interval, GatewayService) {

    $scope.gateways = [];
    $scope.gateway = { identity: '', psk: '' };
    $scope.status = 0; //to do add mongo db status

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

    $scope.showClients = function(identity){
         $scope.status = 1;
    };


    $scope.reset = function() {
        $scope.gateway = { identity: '', psk: '' };
        $scope.searchForm.$setPristine(); //reset Form
    };



}]);