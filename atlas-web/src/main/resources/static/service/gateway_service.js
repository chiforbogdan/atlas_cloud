
angular.module('atlas').factory('GatewayService', ['$http', '$q', function ($http, $q) {

    var REST_SERVICE_URI = 'http://localhost:10000/atlas/gateways';

    var factory = {
        fetchAllGateways: fetchAllGateways
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
}]);