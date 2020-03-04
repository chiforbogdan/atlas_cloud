
angular.module('atlas').factory('GatewayService', ['$http', '$q', function ($http, $q) {

    var REST_SERVICE_URI = 'https://localhost:10000/atlas/gateways';
    var REST_SERVICE_URI_ADD_GATEWAY = 'https://localhost:10000/atlas/gateway/add';
    var factory = {
        fetchAllGateways: fetchAllGateways,
        createGateway: createGateway
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

    function createGateway(gateway) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI_ADD_GATEWAY, gateway)
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