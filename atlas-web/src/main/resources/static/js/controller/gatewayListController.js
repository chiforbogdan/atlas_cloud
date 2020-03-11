'use strict'

atlas_app.controller('GatewayController',[ '$scope', '$interval', 'GatewayService', function($scope, $interval, GatewayService) {

    $scope.gateways = []; //all the gateways
    $scope.gateway = { alias: '', identity: '', psk: '' }; //the gw from form

    fetchAllGateways();

    /*
    * Get updates of gw data by polling //to do WebSocket
    */
    var fetchAllGatewaysInterval = $interval(function() { fetchAllGateways() }, 2000);

    /*
    * Add a new gateway submit function
    */
    $scope.submit = function() {
        console.log('Saving New Gateway', $scope.gateway);
        GatewayService.createGateway($scope.gateway);
        $scope.reset();
    };

    /*
    * Reset the form input text boxes
    */
    $scope.reset = function() {
        $scope.gateway = { alias: '', identity: '', psk: '' };
        $scope.searchForm.$setPristine(); //reset Form
    };

    /*
    * Fetch the all the gateways using the GatewayService
    */
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

    /*
    * On destruction event of the controller, cancel the $interval service that makes the polling
    */
    $scope.$on('$destroy', function() {
        if(angular.isDefined(fetchAllGatewaysInterval)) {
             $interval.cancel(fetchAllGatewaysInterval);
        }
    });

}]);

atlas_app.controller('RowCtrl', function ($scope) {

     $scope.toggleRow = function () {
          $scope.selected = !$scope.selected;
     };

     $scope.isSelected = function (i) {
         return $scope.selected;
     };
});