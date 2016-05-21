(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Team', Team);

    Team.$inject = ['$resource'];

    function Team ($resource) {
        var resourceUrl =  'api/events/:year/teams/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    console.log(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
