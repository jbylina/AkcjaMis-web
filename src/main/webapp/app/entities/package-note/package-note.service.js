(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('PackageNote', PackageNote);

    PackageNote.$inject = ['$resource'];

    function PackageNote ($resource) {
        var resourceUrl =  'api/package-notes/:id';

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
