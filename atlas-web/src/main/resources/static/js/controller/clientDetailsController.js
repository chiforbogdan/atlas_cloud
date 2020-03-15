'use strict'

atlas_app.controller('ClientDetailsController',[ '$scope', '$interval', '$route', 'GatewayService', function($scope, $interval, $route, GatewayService) {

    $scope.gw_identity = $route.current.params.id1; //selected gw's psk
    $scope.cl_identity = $route.current.params.id2; //selected client's identity
    $scope.client = ''; //fetched client


    fetchClientDetails($scope.gw_identity, $scope.cl_identity);

    /*
    * Get updates of gw data by polling //to do WebSocket
    */
    var fetchClientDetailsInterval = $interval(function() { fetchClientDetails($scope.gw_identity, $scope.cl_identity) }, 2000);

    /*
    * Fetch the client details
    * @param gw_identity selected gw's identity
    * @param cl_identity selected client's identity
    */
    function fetchClientDetails(gw_identity, cl_identity){
        GatewayService.fetchClientDetails(gw_identity, cl_identity)
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