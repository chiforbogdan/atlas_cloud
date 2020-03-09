'use strict'

atlas_app.controller('GatewayController',[ '$scope', '$interval', '$window', 'GatewayService', function($scope, $interval, $window, GatewayService) {

    $scope.gateways = [];
    $scope.gateway = { identity: '', psk: '' };

    fetchAllGateways();

    // TO DO!! WebSOCKET -->now fetch only one time
     /*$interval(function() {
            fetchAllGateways()
         }, 2000)*/

    $scope.submit = function() {

        console.log('Saving New Gateway', $scope.gateway);

        GatewayService.createGateway($scope.gateway);

        $scope.reset();
    };

    $scope.reset = function() {
        $scope.gateway = { identity: '', psk: '' };
        $scope.searchForm.$setPristine(); //reset Form
    };

     function fetchAllGateways() {
         GatewayService.fetchAllGateways()
             .then(
                function (d) {
                     $scope.gateways = d;
                },
                function (errResponse) {
                     console.error('Error while fetching gateways!');
                }
             );
     }

}]);

atlas_app.controller('RowCtrl', function ($scope) {

     $scope.toggleRow = function () {
          $scope.selected = !$scope.selected;
     };

     $scope.isSelected = function (i) {
         return $scope.selected;
     };
});