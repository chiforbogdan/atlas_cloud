'use strict'

atlas_app.controller('ClientsController',[ '$scope', '$interval', '$route', 'GatewayService', function($scope, $interval, $route, GatewayService) {

    $scope.clients = []; //all the clients of the selected gw
    $scope.gateway_psk = $route.current.params.id; //the selected gw's psk

    fetchAllClients($scope.gateway_psk);

    /*
    * Get updates of clients data by polling //to do WebSocket
    */
     var fetchAllClientsInterval = fetchAllClientsInterval = $interval(function() {
            fetchAllClients($scope.gateway_psk)
         }, 2000);

    /*
    * Fetch the clients using the GatewayService
    * @param psk selected gw's psk
    */
    function fetchAllClients(psk){
        GatewayService.fetchAllClients(psk)
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