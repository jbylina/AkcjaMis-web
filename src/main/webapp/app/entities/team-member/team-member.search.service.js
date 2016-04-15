(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('TeamMemberSearch', TeamMemberSearch);

    TeamMemberSearch.$inject = ['$resource'];

    function TeamMemberSearch($resource) {
        var resourceUrl =  'api/_search/team-members/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
