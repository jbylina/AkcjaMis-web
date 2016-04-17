(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Subpackage', Subpackage);

    Subpackage.$inject = ['$resource'];

    function Subpackage ($resource) {
        var resourceUrl =  'api/subpackages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
