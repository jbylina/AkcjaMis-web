(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('TeamMember', TeamMember);

    TeamMember.$inject = ['$resource'];

    function TeamMember ($resource) {
        var resourceUrl =  'api/team-members/:id';

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
