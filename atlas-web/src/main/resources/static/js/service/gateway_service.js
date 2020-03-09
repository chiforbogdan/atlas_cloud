'use strict'

atlas_app.factory('GatewayService', ['$http', '$q', function ($http, $q) {

    var REST_SERVICE_URI = 'http://localhost:10000/atlas/gateways';
    var REST_SERVICE_URI_GATEWAY = 'http://localhost:10000/atlas/gateway/';

    var factory = {
        fetchAllGateways: fetchAllGateways,
        createGateway: createGateway,
        fetchAllClients: fetchAllClients,
        fetchClientDetails: fetchClientDetails
    };

    return factory;

    function fetchAllGateways() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching Gateways');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchAllClients(psk) {
         var deferred = $q.defer();
         $http.get(REST_SERVICE_URI_GATEWAY + 'clients/' + psk)
             .then(
                 function (response) {
                     deferred.resolve(response.data);
                 },
                 function (errResponse) {
                     console.error('Error while fetching clients!');
                     deferred.reject(errResponse);
                 }
             );
         return deferred.promise;
    }

    function fetchClientDetails(psk, client_identity) {
         var deferred = $q.defer();
         $http.get(REST_SERVICE_URI_GATEWAY + 'client/' + psk + '/' + client_identity)
             .then(
                 function (response) {
                     deferred.resolve(response.data);
                 },
                 function (errResponse) {
                     console.error('Error while fetching client details!');
                     deferred.reject(errResponse);
                 }
             );
         return deferred.promise;
    }

    function createGateway(gateway) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI_GATEWAY + 'add', gateway)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while creating Gateway');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);