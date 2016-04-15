(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('SubpackageNote', SubpackageNote);

    SubpackageNote.$inject = ['$resource'];

    function SubpackageNote ($resource) {
        var resourceUrl =  'api/subpackage-notes/:id';

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
