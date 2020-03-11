'use strict'

atlas_app.controller('ClientDetailsController',[ '$scope', '$interval', '$route', 'GatewayService', function($scope, $interval, $route, GatewayService) {

    $scope.gateway_psk = $route.current.params.id1; //selected gw's psk
    $scope.client_identity = $route.current.params.id2; //selected client's identity
    $scope.client = ''; //fetched client


    fetchClientDetails($scope.gateway_psk, $scope.client_identity);

    /*
    * Get updates of gw data by polling //to do WebSocket
    */
    var fetchClientDetailsInterval = $interval(function() { fetchClientDetails($scope.gateway_psk, $scope.client_identity) }, 2000);

    /*
    * Fetch the client details
    * @param psk selected gw's psk
    * @param client selected client's identity
    */
    function fetchClientDetails(psk, client){
        GatewayService.fetchClientDetails(psk, client)
             .then(
                function (d) {
                     $scope.client = d;
                },
                function (errResponse) {
                     console.error('Error while fetching client details!');
                }
         );
    }

    /*
    * On destruction event of the controller, cancel the $interval service that makes the polling
    */
   $scope.$on('$destroy', function() {
        if(angular.isDefined(fetchClientDetailsInterval)) {
             $interval.cancel(fetchClientDetailsInterval);
        }
   });
}]);