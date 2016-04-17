(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageNote', ChristmasPackageNote);

    ChristmasPackageNote.$inject = ['$resource'];

    function ChristmasPackageNote ($resource) {
        var resourceUrl =  'api/christmas-package-notes/:id';

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
