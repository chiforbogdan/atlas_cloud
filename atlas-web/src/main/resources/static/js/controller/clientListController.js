'use strict'

atlas_app.controller('ClientsController',[ '$scope', '$interval', '$route', 'GatewayService', function($scope, $interval, $route, GatewayService) {

    $scope.clients = []; //all the clients of the selected gw
    $scope.gw_identity = $route.current.params.id1; //the selected gw's psk
    $scope.gw_alias = $route.current.params.id2; //he selected gw's alias

    fetchAllClients($scope.gw_identity);

    /*
    * Get updates of clients data by polling //to do WebSocket
    */
     var fetchAllClientsInterval = fetchAllClientsInterval = $interval(function() {
            fetchAllClients($scope.gw_identity)
         }, 2000);

    /*
    * Fetch the clients using the GatewayService
    * @param gw_identity selected gw's identity
    */
    function fetchAllClients(gw_identity){
        GatewayService.fetchAllClients(gw_identity)
             .then(
                function (d) {
                     $scope.clients = d;
                },
                function (errResponse) {
                     console.error('Error while fetching clients!');
                }
         );
    }
    /*
    * On destruction event of the controller, cancel the $interval service that makes the polling
    */
   $scope.$on('$destroy', function() {
        if(angular.isDefined(fetchAllClientsInterval)) {
             $interval.cancel(fetchAllClientsInterval);
        }
   });

}]);