'use strict'

atlas_app.controller('ClientDetailsController',[ '$scope', '$route', 'GatewayService', function($scope, $route, GatewayService) {

    $scope.gateway_psk = $route.current.params.id1;
    $scope.client_identity = $route.current.params.id2;
    $scope.client = '';


    fetchClientDetails($scope.gateway_psk, $scope.client_identity);

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
}]);