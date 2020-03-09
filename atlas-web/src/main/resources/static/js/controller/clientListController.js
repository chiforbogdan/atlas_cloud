'use strict'

atlas_app.controller('ClientsController',[ '$scope', '$interval', '$route', 'GatewayService', function($scope, $interval, $route, GatewayService) {

    $scope.clients = [];
    $scope.gateway_psk = $route.current.params.id;
    $scope.client = '';

    fetchAllClients($scope.gateway_psk);

   // TO DO!! WebSOCKET -->now fetch only one time
   /* $interval(function() {
            fetchAllClients($scope.gateway_psk)
         }, 2000)*/

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

}]);