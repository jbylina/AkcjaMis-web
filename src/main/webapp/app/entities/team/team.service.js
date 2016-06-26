(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Team', Team);

    Team.$inject = ['$resource'];

    function Team ($resource) {
        var resourceUrl =  'api/events/:year/teams';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
                isArray: true},
            'get': {
                method: 'GET',
                url : 'api/events/:year/teams/:id'
            },
            'update': { method:'PUT'}
        });
    }
})();
