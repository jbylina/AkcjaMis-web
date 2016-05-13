(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('ChristmasPackage', ChristmasPackage);

    ChristmasPackage.$inject = ['$resource'];

    function ChristmasPackage ($resource) {
        var resourceUrl =  '/api/events/:event_id/christmas-packages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getList': {
                method: 'GET',
                url: '/api/events/:event_id/christmas-packages-list',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'save': { method: 'POST' },
            'delete': { method: 'DELETE' }
        });
    }
})();
