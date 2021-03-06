(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Contact', Contact);

    Contact.$inject = ['$resource'];

    function Contact ($resource) {
        var resourceUrl =  '/api/families/:id/contacts';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {
                method:'PUT'
            },
            'delete': {
                method:'DELETE',
                url: '/api/families/:familyId/contacts/:id'
            }
        });
    }
})();
