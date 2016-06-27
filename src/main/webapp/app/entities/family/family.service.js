(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Family', Family);

    Family.$inject = ['$resource'];

    function Family ($resource) {
        var resourceUrl =  'api/families/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT',
                url: '/api/families',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }},
            'getPackages': {
                method: 'GET',
                url : 'api/families/:id/christmas-packages',
                isArray: true
            },
            'addToEvent': {
                method: 'PUT',
                url : 'api/families/:id/add-to-event'
            }
        });
    }
})();
