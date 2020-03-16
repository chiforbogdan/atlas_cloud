'use strict'

atlas_app.controller('GatewayController',[ '$scope', '$interval', '$uibModal', 'GatewayService', function($scope, $interval, $uibModal, GatewayService) {

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
    * Delete gateway
    */
    $scope.remove = function(gw_identity){
        $uibModal.open({
          templateUrl: 'view/modal.html',
          controller: function ($scope, $uibModalInstance) {
            $scope.ok = function () {
              GatewayService.deleteGateway(gw_identity);
              $uibModalInstance.close();
            };

            $scope.cancel = function () {
              $uibModalInstance.dismiss('cancel');
            };
          }
        })
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

    var expanded_hash_map = {}; //memorize the state of the row

    /*
    * Expand row | Shrink row
    */
     $scope.toggleRow = function (i) {
          expanded_hash_map[i] = !expanded_hash_map[i];
     };

     $scope.isSelected = function (i) {
         return  expanded_hash_map[i];
     };

    /*
    * On destruction event of the controller, cancel the $interval service that makes the polling
    */
    $scope.$on('$destroy', function() {
        if(angular.isDefined(fetchAllGatewaysInterval)) {
             $interval.cancel(fetchAllGatewaysInterval);
        }
    });

}]);
