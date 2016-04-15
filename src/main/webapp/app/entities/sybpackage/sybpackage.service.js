(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Sybpackage', Sybpackage);

    Sybpackage.$inject = ['$resource'];

    function Sybpackage ($resource) {
        var resourceUrl =  'api/sybpackages/:id';

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
